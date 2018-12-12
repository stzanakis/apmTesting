package com.tzan.apm.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-12
 */
public class Metric {

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  @JsonProperty("@timestamp")
  private Date timestamp;
  @JsonProperty("@metadata")
  private Metadata metadata = new Metadata();
  @JsonProperty("system")
  private SystemMetrics system = new SystemMetrics();

  public Metric() {
  }

  public Metric(Date timestamp, Metadata metadata, SystemMetrics system) {
    this.timestamp = timestamp;
    this.metadata = metadata;
    this.system = system;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public SystemMetrics getSystem() {
    return system;
  }

  public void setSystem(SystemMetrics system) {
    this.system = system;
  }
}
