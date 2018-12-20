package com.tzan.apm.model;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-11
 */
public enum MetricLabels {
  TIMESTAMP("timestamp", (metric, value) -> metric
      .setTimestamp(new Date(TimeUnit.NANOSECONDS.toMillis(Long.parseLong(value))))),
  APPLICATION_NAME("applicationName",
      (metric, value) -> metric.getMetadata().setApplicationName(value)),
  APPLICATION_ID("applicationId",
      (metric, value) -> metric.getSystem().setApplicationId(value.replace("\"", ""))),
  INSTANCE_INDEX("instanceIndex",
      (metric, value) -> metric.getSystem().setInstanceIndex(Integer.parseInt(value))),
  CPU_PERCENTAGE("cpuPercentage",
      (metric, value) -> metric.getSystem().setCpuPercentage(Double.parseDouble(value))),
  MEMORY_BYTES("memoryBytes",
      (metric, value) -> metric.getSystem().setMemoryBytes(Long.parseLong(value))),
  DISK_BYTES("diskBytes",
      (metric, value) -> metric.getSystem().setDiskBytes(Long.parseLong(value))),
  MEMORY_BYTES_QUOTA("memoryBytesQuota",
      (metric, value) -> metric.getSystem().setMemoryBytesQuota(Long.parseLong(value))),
  DISK_BYTES_QUOTA("diskBytesQuota",
      (metric, value) -> metric.getSystem().setDiskBytesQuota(Long.parseLong(value)));

  private String nozzleLabel;
  private BiConsumer<Metric, String> contentSetter;

  MetricLabels(String nozzleLabel, BiConsumer<Metric, String> contentSetter) {
    this.nozzleLabel = nozzleLabel;
    this.contentSetter = contentSetter;
  }

  public static MetricLabels getMetricLabelsFromNozzleLabel(String nozzleLabel) {
    for (MetricLabels metricLabel : MetricLabels.values()) {
      if (metricLabel.getNozzleLabel().equalsIgnoreCase(nozzleLabel)) {
        return metricLabel;
      }
    }
    return null;
  }

  public String getNozzleLabel() {
    return nozzleLabel;
  }

  public BiConsumer<Metric, String> getContentSetter() {
    return contentSetter;
  }
}
