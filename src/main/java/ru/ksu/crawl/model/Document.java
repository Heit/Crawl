package ru.ksu.crawl.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Heit
 * Date: 16.03.2010
 * Time: 15:48:26
 */
public class Document implements Serializable {

    private String uri;

    private Long indexDate;

    public Document() {
    }

    public Document(String uri, Long indexDate) {
        this.uri = uri;
        this.indexDate = indexDate;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Long getIndexDate() {
        return indexDate;
    }

    public void setIndexDate(Long indexDate) {
        this.indexDate = indexDate;
    }
}
