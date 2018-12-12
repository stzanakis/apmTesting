package com.tzan.apm.model;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-12
 */
public class Metadata {
  private String type;

  public Metadata() {
  }

  public Metadata(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
