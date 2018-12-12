package com.tzan.apm.model;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-12
 */
public class Metadata {
  private String type;
  private String applicationName;

  public Metadata() {
  }

  public Metadata(String type, String applicationName) {
    this.type = type;
    this.applicationName = applicationName;
  }

  public String getApplicationName() {
    return applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
