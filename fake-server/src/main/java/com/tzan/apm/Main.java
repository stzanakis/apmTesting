package com.tzan.apm;

import com.tzan.apm.model.Metric;
import com.tzan.apm.utils.DataUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-10
 */
public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

//  public static final String APPLICATION_NAME = "metis-authentication-rest-test";
  public static final List<String> applicationNames = new ArrayList<>(
      Arrays.asList("metis-authentication-rest-test", "metis-core-rest-test"));
  private static final String APP_NOZZLE_COMMAND_TEMPLATE = "cf app-nozzle %s -filter ContainerMetric";
  private static Map<String, Thread> runnablesMap = new HashMap<>();
  private static final int SLEEP_TIME_PER_LOOP_IN_MINS = 1;

  public static void main(String[] arg) throws IOException, InterruptedException {

    do {
      LOGGER.info("Start loop to check running threads");

      //Read application names from file every minute
      //Check which ones are not running and start them
      for (String applicationName : applicationNames) {
        final Thread thread = runnablesMap.get(applicationName);
        if (thread == null || !thread.isAlive()) {
          final Thread runnableThread = createRunnableThread(applicationName);
          runnablesMap.put(applicationName, runnableThread);
          runnableThread.start();
          LOGGER.info("Started thread for applicationName: {}.", applicationName);
        } else {
          LOGGER.info("Thread for {}, is alive.", applicationName);
        }
      }

      LOGGER.info("Sleeping for {} before re-checking map of threads.", SLEEP_TIME_PER_LOOP_IN_MINS);
      Thread.sleep(TimeUnit.MINUTES.toMillis(SLEEP_TIME_PER_LOOP_IN_MINS));
    } while (true);

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

  private static Thread createRunnableThread(String applicationName) throws IOException {
    Runtime rt = Runtime.getRuntime();
    final Process process = rt.exec(String.format(APP_NOZZLE_COMMAND_TEMPLATE, applicationName));
    return new Thread(() -> {
      BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;

      try {
        while ((line = input.readLine()) != null) {
          final Metric metric = DataUtils.convertNozzleMetricsToMetricObject(applicationName, line);
          DataUtils.sendMetricToElasticSearch(metric);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

}


