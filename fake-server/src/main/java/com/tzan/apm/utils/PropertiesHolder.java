package com.tzan.apm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-13
 */
public class PropertiesHolder {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesHolder.class);

  private static final String CONFIGURATION_FILE = "configuration.properties";
  private static final Properties properties = new Properties();
  public static final String APP_NOZZLE_COMMAND_TEMPLATE = "cf app-nozzle %s -filter ContainerMetric";
  public static final int SLEEP_TIME_PER_LOOP_IN_MINS = 1;

  private String elasticsearchIndexUrl;
  private String cfApplicationNamesFilePath;

  public PropertiesHolder() {
    InputStream input = null;
    try {
      input = getClass().getClassLoader().getResourceAsStream(CONFIGURATION_FILE);
      properties.load(input);
    } catch (IOException e) {
      LOGGER.error(String.format("Could not read the properties file %s", CONFIGURATION_FILE), e);
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          LOGGER.error(
              String.format("Could not close input of the properties file %s", CONFIGURATION_FILE),
              e);
        }
      }
    }
    initializeProperties();
  }

  private void initializeProperties() {
    elasticsearchIndexUrl = (String) properties.get("elasticsearch.index.url");
    cfApplicationNamesFilePath = (String) properties.get("application.names.file");
  }

  public String getElasticsearchIndexUrl() {
    return elasticsearchIndexUrl;
  }

  public String getCfApplicationNamesFilePath() {
    return cfApplicationNamesFilePath;
  }
}
