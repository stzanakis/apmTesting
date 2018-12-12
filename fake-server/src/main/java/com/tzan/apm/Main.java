package com.tzan.apm;

import com.tzan.apm.model.Metric;
import com.tzan.apm.utils.DataUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-10
 */
public class Main {

  public static final String applicationName = "metis-authentication-rest-test";
  private static final String APP_NOZZLE_COMMAND_TEMPLATE = "cf app-nozzle %s -filter ContainerMetric";

  public static void main(String[] arg) throws IOException, InterruptedException {
    Runtime rt = Runtime.getRuntime();
    final Process pr = rt.exec(String.format(APP_NOZZLE_COMMAND_TEMPLATE, applicationName));
    new Thread(() -> {
      BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      String line;

      try {
        while ((line = input.readLine()) != null) {
          final Metric metric = DataUtils.convertNozzleMetricsToMetricObject(line);
          DataUtils.sendMetricToElasticSearch(metric);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }).start();

    pr.waitFor();

//    final Metric metric = convertNozzleMetricsToMetricObject();
//    final Metric metric = convertNozzleMetricsToMetricObject(
//        "origin:\"rep\" eventType:ContainerMetric timestamp:1544541476899162066 deployment:\"eu-de-worker2-fra04\" job:\"diego-cell\" index:\"1ead2503-2c8d-459d-8b2d-006956a73182\" ip:\"161.156.69.23\" tags:<key:\"source_id\" value:\"bf50f43e-3068-4aed-ac8d-c3e109fa3fca\" > containerMetric:<applicationId:\"bf50f43e-3068-4aed-ac8d-c3e109fa3fca\" instanceIndex:1 cpuPercentage:1.5901474090814591 memoryBytes:724201472 diskBytes:179109888 memoryBytesQuota:2147483648 diskBytesQuota:1073741824 >  ");
//    ObjectMapper mapper = new ObjectMapper();
//    System.out.println(mapper.writeValueAsString(metric));
//    sendMetricToElasticSearch(metric);
//    ObjectMapper mapper = new ObjectMapper();
//    System.out.println(mapper.defaultPrettyPrintingWriter().writeValueAsString(jsonString));
//    System.out.println(mapper.writeValueAsString(metric));
  }

}


