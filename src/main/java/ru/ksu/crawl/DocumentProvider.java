package ru.ksu.crawl;

import com.torunski.crawler.Crawler;
import com.torunski.crawler.filter.ILinkFilter;
import com.torunski.crawler.filter.ServerFilter;
import com.torunski.crawler.model.MaxDepthModel;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.morphology.russian.RussianAnalayzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Repository;
import org.w3c.tidy.Tidy;
import ru.ksu.crawl.model.Document;
import ru.ksu.crawl.model.SearchResult;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Heit
 * Date: 13.03.2010
 * Time: 18:38:39
 */
@Repository("documentProvider")
public class DocumentProvider {

    public static final String[] FILE_EXTENSION_LIST = {
            ".pdf", ".jpg", ".jpeg", ".gif", ".png", ".css", ".js", ".xlt", ".doc", ".ppt"
    };

    public List<Document> indexedUrls = new LinkedList<Document>();

    public static final ILinkFilter FILE_EXTENTION_FILTER = new FileExtensionFilter(FILE_EXTENSION_LIST);

    private String indexLocation = System.getProperty("java.io.tmpdir") + File.separator + "crawler";

    public List<ru.ksu.crawl.model.Document> crawl(String host, String urlToBegin) {
        List<ru.ksu.crawl.model.Document> crawlResult = new LinkedList<ru.ksu.crawl.model.Document>();
        Crawler crawler = new Crawler();
        IndexWriter writer = null;
        try {
            FSDirectory directory = FSDirectory.open(new File(indexLocation));
            writer = new IndexWriter(directory, new RussianAnalayzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexHtmlEventListener htmlEventListener = new IndexHtmlEventListener(writer, crawlResult);
        crawler.setParser(new KsuHttpParser());
        crawler.setLinkFilter(new ServerFilter(host));
        crawler.setModel(new MaxDepthModel(1));
        crawler.addParserListener(htmlEventListener);
        crawler.start(host, urlToBegin);
        try {
            if (writer != null) {
                writer.optimize();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crawlResult;
    }

    public List<SearchResult> search(String queryString) {
        FSDirectory directory = null;
        List<SearchResult> searchResults = new LinkedList<SearchResult>();
        try {
            directory = FSDirectory.open(new File(indexLocation));
            IndexSearcher searcher = new IndexSearcher(directory);
            QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "body", new RussianAnalayzer());
            Query query = null;
            try {
                query = parser.parse(queryString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (query != null) {
                query = query.rewrite(searcher.getIndexReader());
            }
            org.apache.lucene.search.highlight.Scorer scorer = new QueryScorer(query);
            TopDocs rs = searcher.search(query, null, 10);
            ScoreDoc[] scoreDocsArray = rs.scoreDocs;
            for (ScoreDoc scoredoc : scoreDocsArray){
                System.out.println(searcher.doc(scoredoc.doc).get("url")+" "+scoredoc.score);
                SearchResult searchResult = new SearchResult();
                searchResult.setUri(searcher.doc(scoredoc.doc).get("url") +", Score: "+scoredoc.score);
                searchResult.setData(searcher.doc(scoredoc.doc).get("path"));
                searchResults.add(searchResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }


    public String highlightResults(String queryString, String path) {
        String body = null;
        try {
            QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "body", new RussianAnalayzer());
            Query query = parser.parse(queryString);
            Scorer scorer = new QueryScorer(query);
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<i><font color='#ff3366'>", "</font></i>");
            Highlighter highlighter = new Highlighter(formatter, scorer);
            Fragmenter fragmenter = new NullFragmenter();
            highlighter.setTextFragmenter(fragmenter);
            body = readFileAsString(path);
            TokenStream tokenStream = new RussianAnalayzer().tokenStream("body", new StringReader(body));
            String result = highlighter.getBestFragments(tokenStream, body, 5, "...").trim();
            System.out.println(result);
            return result;
        } catch (ParseException pe) {
            pe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readFileAsString(String filePath) throws java.io.IOException {
        byte[] buffer = new byte[(int) new File(filePath).length()];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));
        f.read(buffer);
        String html = new String(buffer, "UTF-8");
        Pattern p = Pattern.compile(".*<img[^>]*src=\"([^\"]*)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(html);
        html = m.replaceAll("");
        return html.replaceAll("\\<.*?\\>", "").replaceAll("\r", "").replaceAll("\n", " ").replaceAll("  ", " ").replaceAll("src", "");
    }


}
