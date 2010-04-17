package ru.ksu.crawl;

import com.torunski.crawler.link.Link;
import com.torunski.crawler.parser.PageData;
import com.torunski.crawler.parser.httpclient.HttpClientUtil;
import com.torunski.crawler.parser.httpclient.PageDataHttpClient;
import com.torunski.crawler.parser.httpclient.SimpleHttpClientParser;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.DateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: heit
 * Date: 07.04.2010
 * Time: 0:48:09
 * To change this template use File | Settings | File Templates.
 */
public class KsuHttpParser extends SimpleHttpClientParser {

    private static final transient Log LOG = LogFactory.getLog(KsuHttpParser.class);

    @Override
    public PageData load(Link link) {
        String uri = link.getURI();
        LOG.info("download: " + uri);

        // Create a method instance.
        GetMethod httpGet = null;
        try {
            httpGet = new GetMethod(uri);
            httpGet.setFollowRedirects(false);
        } catch (Exception e) {
            LOG.info("HTTP get failed for " + uri);
            return new PageDataHttpClient(link, PageData.ERROR);
        }

        // Provide a custom retry handler
        httpGet.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(5, false));
        // httpGet.getParams().setIntParameter(HttpMethodParams.BUFFER_WARN_TRIGGER_LIMIT, 1024*1024);
        httpGet.setRequestHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; ru; rv:1.9.0.18) Gecko/2010021501 Ubuntu/9.04 (jaunty) Firefox/3.0.18");

        if (link.getTimestamp() > 0) {
            httpGet.setRequestHeader(HttpClientUtil.HEADER_IF_MODIFIED_SINCE, DateUtil.formatDate(new Date(link.getTimestamp())));
        }

        int statusCode = 0;
        String responseBody = null;
        try {
            // Execute the method
            statusCode = client.executeMethod(httpGet);

            if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
                LOG.info("Content not modified since last request of " + uri);
            } else if (HttpClientUtil.isRedirect(statusCode)) {
                final URI redirect = HttpClientUtil.getRedirectURI(new URI(link.getURI(), false), httpGet);
                if (redirect != null) {
                    responseBody = redirect.getURI();
                    LOG.info("Redirect found for " + uri + " to " + redirect);
                } else {
                    statusCode = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    LOG.warn("Invalid redirect for " + uri);
                }
            } else if (statusCode != HttpStatus.SC_OK) {
                LOG.info("Method failed: " + httpGet.getStatusLine() + " for " + uri);
            } else if (!containsText(httpGet)) {
                LOG.warn("URL does not contain text or content-type is wrong of " + uri);
                httpGet.abort();
            } else {
                // read the response body as a stream
                responseBody = httpGet.getResponseBodyAsString();
                // don't overwrite the values of the given link object
                link = new Link(httpGet.getURI().getURI());
                link.setTimestamp(HttpClientUtil.getLastModified(httpGet));
            }
        } catch (IOException e) {
            responseBody = null;
            LOG.warn("Failed reading from uri=" + uri, e);
        } finally {
            // Release the connection.
            httpGet.releaseConnection();
        }

        if (HttpClientUtil.isRedirect(statusCode)) {
            PageDataHttpClient pageDataHttpClient = new PageDataHttpClient(link, PageData.REDIRECT);
            pageDataHttpClient.setData(responseBody);
            return pageDataHttpClient;
        } else if (responseBody != null) {
            return new PageDataHttpClient(link, responseBody, httpGet.getResponseCharSet());
        } else {
            if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
                return new PageDataHttpClient(link, PageData.NOT_MODIFIED);
            } else {
                return new PageDataHttpClient(link, PageData.ERROR);
            }
        }
    }

    private boolean containsText(HttpMethodBase method) {
        Header contentType = method.getResponseHeader("content-type");
        if (contentType != null) {
            HeaderElement[] elements = contentType.getElements();
            for (int i = 0; i < elements.length; i++) {
                String name = elements[i].getName();
                if ((name != null) && (name.startsWith("text"))) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
