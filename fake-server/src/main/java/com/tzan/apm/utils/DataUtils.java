package com.tzan.apm.utils;

import com.tzan.apm.Main;
import com.tzan.apm.model.Metric;
import com.tzan.apm.model.MetricLabels;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-12
 */
public class DataUtils {

  private static final String baseUrl = "http://prod30.crf.europeana.eu:9200/index/metrics";

  public static void sendMetricToElasticSearch(Metric metric) {
    if (metric != null) {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      HttpEntity<Metric> entity = new HttpEntity<>(metric, headers);
      restTemplate.postForEntity(baseUrl, entity, Metric.class);
      System.out.println("POSTED");
    }
  }

  public static Metric convertNozzleMetricsToMetricObject(String nozzleMetricLine) {
    if (nozzleMetricLine != null && nozzleMetricLine.contains("containerMetric:<")) {
      final String metricStartingFromTimestamp = nozzleMetricLine
          .substring(nozzleMetricLine.indexOf("timestamp:") + "timestamp:".length());
      final String timestampValue = metricStartingFromTimestamp
          .substring(0, metricStartingFromTimestamp.indexOf(" "));
      final String metrics = nozzleMetricLine.split("containerMetric:<")[1];
      final String[] metricsArray = metrics.split(" ");

      return createMetricObjectOutOfMetricsArray(metricsArray,
          timestampValue);
    }

    return null;
  }

  private static Metric createMetricObjectOutOfMetricsArray(String[] metricsArray,
      String timestampValue) {
    Metric metric = new Metric();
    metric.setTimestamp(new Date(TimeUnit.NANOSECONDS.toMillis(Long.parseLong(timestampValue))));
    metric.getMetadata().setType("customMetric");
    metric.getSystem().setApplicationName(Main.applicationName);

    for (String metricField : metricsArray) {
      if (metricField.contains(":")) {
        final String[] split = metricField.split(":");
        addMetricToSystemMetrics(metric, split[0], split[1]);

      }
    }
    return metric;
  }

  private static void addMetricToSystemMetrics(Metric metric, String metricLabel,
      String metricValue) {
    final MetricLabels metricLabelsFromNozzleLabel = MetricLabels
        .getMetricLabelsFromNozzleLabel(metricLabel);
    if (metricLabelsFromNozzleLabel != null) {
      metricLabelsFromNozzleLabel.getContentSetter().accept(metric, metricValue);
    }
  }
}
