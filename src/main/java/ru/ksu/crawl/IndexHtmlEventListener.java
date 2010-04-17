package ru.ksu.crawl;

import com.torunski.crawler.events.IParserEventListener;
import com.torunski.crawler.events.ParserEvent;
import com.torunski.crawler.filter.ILinkFilter;
import com.torunski.crawler.link.Link;
import com.torunski.crawler.parser.PageData;
import com.torunski.crawler.util.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.demo.HTMLDocument;
import org.apache.lucene.demo.html.HTMLParser;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.cyberneko.html.parsers.DOMParser;
import org.cyberneko.html.parsers.SAXParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Heit
 * Date: 16.03.2010
 * Time: 15:04:50
 */
public class IndexHtmlEventListener implements IParserEventListener {

  private static final transient Log LOG = LogFactory.getLog(IndexHtmlEventListener.class);

  private IndexWriter writer;

  private List<ru.ksu.crawl.model.Document> documents;

  private ILinkFilter filter = DocumentProvider.FILE_EXTENTION_FILTER;

  public IndexHtmlEventListener(IndexWriter writer, List<ru.ksu.crawl.model.Document> documents) {
    this.writer = writer;
    this.documents = documents;
  }

  public static final String FIELD_URL = "url";

  public static final String FIELD_TIMESTAMP = "timestamp";

  public Document createDocument(File file, Link link) {
    try {

      Document doc = new Document();

      // Add the url as a field named "path".  Use a field that is
      // indexed (i.e. searchable), but don't tokenize the field into words.
      doc.add(new Field("path", file.getPath(), Field.Store.YES,
          Field.Index.NOT_ANALYZED));

      doc.add(new Field(FIELD_URL, link.getURI(), Field.Store.YES, Field.Index.NO));

      doc.add(new Field("modified",
          DateTools.timeToString(file.lastModified(), DateTools.Resolution.MINUTE),
          Field.Store.YES, Field.Index.NOT_ANALYZED));

      doc.add(new Field(FIELD_TIMESTAMP,
          DateTools.timeToString(link.getTimestamp(), DateTools.Resolution.MILLISECOND),
          Field.Store.YES, Field.Index.NOT_ANALYZED));


      DOMParser parser = new DOMParser();
      try {
        parser.parse(new InputSource(new InputStreamReader(new FileInputStream(file),"UTF-8")));
        doc.add(new Field("body", parser.getDocument().getElementsByTagName("body").item(0).getTextContent(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("title", parser.getDocument().getElementsByTagName("title").item(0).getTextContent(), Field.Store.YES, Field.Index.ANALYZED));
      } catch (SAXException e) {
        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
      documents.add(new ru.ksu.crawl.model.Document(link.getURI(), link.getTimestamp()));
      return doc;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void parse(ParserEvent event) {
    PageData pageData = event.getPageData();
    System.out.println("Start parsing: " + pageData.getLink().getURI());
    String path = pageData.getLink().getURI();
    if ((filter == null) || (filter.accept(path, path))) {
      File file = null;
      try {
        file = File.createTempFile("crawler", null);
        Charset iso88591charset = Charset.forName("ISO-8859-1");
        Locale.setDefault(new Locale("ru_RU"));
        LOG.debug("Temp file " + file);
        Object data = pageData.getData();
        if (data instanceof String) {
          Writer out = new BufferedWriter(
              new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
          out.write((String) data);
          out.close();
        } else {
          LOG.warn("Page data has to be stored as a string object. link=" + path);
        }
        indexFile(file, pageData.getLink());
//                finally {
//                    if (!file.delete()) {
//                        LOG.warn("Can't delete temporary file=" + file);
//                    }
//                }
      } catch (IOException e) {
        LOG.error("Error creating or deleting temporary file=" + file, e);
      }
    }
  }


  public void indexFile(File file, Link link) {
    try {
      Document doc = createDocument(file, link);
      if (doc != null) {
        writer.addDocument(doc);
      }
    } catch (IOException e) {
      LOG.error("IOException indexing temporary file=" + file, e);
    }
  }
}
