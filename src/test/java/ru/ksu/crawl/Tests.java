package ru.ksu.crawl;

import com.torunski.crawler.Crawler;
import com.torunski.crawler.filter.ServerFilter;
import com.torunski.crawler.model.MaxDepthModel;
import junit.framework.TestCase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.morphology.russian.RussianAnalayzer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Heit
 * Date: 16.03.2010
 * Time: 14:50:26
 */
public class Tests extends TestCase {

    private String host = "http://kursksu.ru";

    private String basepath = "/pages/choose/1a.shtml";

    private String indexLocation = System.getProperty("java.io.tmpdir") + File.separator + "crawler";

    public void testCrawl() {
//        List<ru.ksu.crawl.model.Document> crawlResult = new LinkedList<ru.ksu.crawl.model.Document>();
//        Crawler crawler = new Crawler();
//        IndexWriter writer = null;
//        try {
//            FSDirectory directory = FSDirectory.open(new File(indexLocation));
//            writer = new IndexWriter(directory, new RussianAnalayzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        IndexHtmlEventListener htmlEventListener = new IndexHtmlEventListener(writer,crawlResult);
//        crawler.setParser(new KsuHttpParser());
//        crawler.setLinkFilter(new ServerFilter(host));
//        crawler.setModel(new MaxDepthModel(1));
//        crawler.addParserListener(htmlEventListener);
//        crawler.start(host, basepath);
//        try {
//            if (writer != null) {
//                writer.optimize();
//                writer.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void testSearch() {
//        FSDirectory directory = null;
//        try {
//            directory = FSDirectory.open(new File(indexLocation));
//            IndexSearcher searcher = new IndexSearcher(directory);
//            Query query = new TermQuery(new Term("body", "победитель"));
//            query = query.rewrite(searcher.getIndexReader());
//            org.apache.lucene.search.highlight.Scorer scorer = new QueryScorer(query);
//            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span class=\"highlight\">", "</span>");
//            Highlighter highlighter = new Highlighter(formatter, scorer);
//            Fragmenter fragmenter = new SimpleFragmenter(60);
//            highlighter.setTextFragmenter(fragmenter);
//            TopDocs rs = searcher.search(query, null, 10);
//            for (int i = 0; i < rs.totalHits; i++){
//                Document doc = searcher.doc(i);
//                String body = doc.get("body");
//                TokenStream tokenStream = new RussianAnalayzer().tokenStream("body",  new StringReader(body));
//                try {
//                    String bestFragments = highlighter.getBestFragments(tokenStream, body, 5, "...");
//                    System.out.println(doc.get("url"));
//                    System.out.println(bestFragments);
//                } catch (InvalidTokenOffsetsException e) {
//                    e.printStackTrace();
//                }
//            }
////            System.out.println(rs.totalHits);
//
//        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }



    }

}
