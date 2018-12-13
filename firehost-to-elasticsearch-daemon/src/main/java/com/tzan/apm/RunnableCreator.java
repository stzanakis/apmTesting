package com.tzan.apm;

import com.tzan.apm.model.Metric;
import com.tzan.apm.utils.DataUtils;
import com.tzan.apm.utils.PropertiesHolder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-13
 */
public class RunnableCreator implements Function<String, Runnable> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RunnableCreator.class);
  private final String elasticsearchIndexUrl;

  public RunnableCreator(String elasticsearchIndexUrl) {
    this.elasticsearchIndexUrl = elasticsearchIndexUrl;
  }

  @Override
  public Runnable apply(String applicationName) {
    return () -> {
      Runtime rt = Runtime.getRuntime();
      final Process process;
      try {
        process = rt
            .exec(String
                .format(PropertiesHolder.APP_NOZZLE_COMMAND_TEMPLATE, applicationName));
        commandOutputInfiniteReader(applicationName, process);
      } catch (IOException e) {
        LOGGER.error(String
            .format(
                "Execution of Firehose nozzle command %s failed or command output could not be read",
                applicationName), e);
      }
      LOGGER
          .info(
              "Thread responsible for {}, exiting because it was interrupted or an exception occurred!",
              applicationName);
    };
  }

  private void commandOutputInfiniteReader(String applicationName, Process process)
      throws IOException {
    try (BufferedReader input = new BufferedReader(
        new InputStreamReader(process.getInputStream()))) {
      String line;
      while ((line = input.readLine()) != null) {
        LOGGER.debug("Command response by line: {}", line);
        final Metric metric = DataUtils
            .convertNozzleMetricsToMetricObject(applicationName, line);
        DataUtils
            .sendMetricToElasticSearch(elasticsearchIndexUrl, metric);
        //Exit loop if thread was interrupted
        if (Thread.interrupted()) {
          break;
        }
      }
    }
  }
}
