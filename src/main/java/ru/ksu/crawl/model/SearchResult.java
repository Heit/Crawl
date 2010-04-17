package ru.ksu.crawl.model;

import java.io.Serializable;

/**
 * User: Heit
 * Date: 11.04.2010
 * Time: 14:39:32
 */
public class SearchResult implements Serializable {

  private String uri;

  private String data;

  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
