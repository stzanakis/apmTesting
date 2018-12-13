package com.tzan.apm;

import com.tzan.apm.utils.PropertiesHolder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-10
 */
public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static Map<String, Thread> runnablesMap = new HashMap<>();
  private static final PropertiesHolder PROPERTIES_HOLDER = new PropertiesHolder();

  public static void main(String[] arg) throws InterruptedException {

    List<String> previousLoopCfApplicationNames = new ArrayList<>();
    List<String> cfApplicationNames;
    do {
      LOGGER.info("Start loop to check running threads");
      cfApplicationNames = getCfApplicationNamesFromFile();
      LOGGER.info("Got {} cfApplicationNames from file:", cfApplicationNames.size());
      checkAndStartThreadRequested(cfApplicationNames);
      stopThreadsNotInList(previousLoopCfApplicationNames, cfApplicationNames);

      previousLoopCfApplicationNames = cfApplicationNames;
      LOGGER.info("Sleeping for {} minute before re-checking map of threads.",
          PropertiesHolder.SLEEP_TIME_PER_LOOP_IN_MINS);
      Thread.sleep(TimeUnit.MINUTES.toMillis(PropertiesHolder.SLEEP_TIME_PER_LOOP_IN_MINS));
    } while (true);
  }

  private static void checkAndStartThreadRequested(List<String> cfApplicationNames) {
    //Check which ones are not running and start them
    for (String applicationName : cfApplicationNames) {
      final Thread thread = runnablesMap.get(applicationName);
      if (thread == null || !thread.isAlive()) {
        final Thread runnableThread = new Thread(
            new RunnableCreator(PROPERTIES_HOLDER.getElasticsearchIndexUrl())
                .apply(applicationName));
        runnablesMap.put(applicationName, runnableThread);
        runnableThread.start();
        LOGGER.info("Started thread for applicationName: {}.", applicationName);
      } else {
        LOGGER.info("Thread for {}, is alive.", applicationName);
      }
    }
  }

  private static void stopThreadsNotInList(List<String> previousLoopCfApplicationNames,
      List<String> cfApplicationNames) {
    //Stop the ones that do not exist in the file anymore
    List<String> cfApplicationNamesToStop = new ArrayList<>(previousLoopCfApplicationNames);
    cfApplicationNamesToStop.removeAll(cfApplicationNames);
    for (String applicationName : cfApplicationNamesToStop) {
      final Thread thread = runnablesMap.get(applicationName);
      LOGGER.info("Interrupting thread responsible for {}. Is not in the applicationNames list",
          applicationName);
      thread.interrupt();
    }
  }

  private static List<String> getCfApplicationNamesFromFile() {
    try (Stream<String> stream = Files
        .lines(Paths.get(PROPERTIES_HOLDER.getCfApplicationNamesFilePath()))) {
      return stream.filter(Strings::isNotBlank).collect(Collectors.toList());
    } catch (IOException e) {
      LOGGER.error(String.format("Could not read lines of file %s",
          PROPERTIES_HOLDER.getCfApplicationNamesFilePath()), e);
    }
    return new ArrayList<>();
  }

}


