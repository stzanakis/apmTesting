package com.tzan.apm.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Simon Tzanakis (Simon.Tzanakis@europeana.eu)
 * @since 2018-12-13
 */
public class PropertiesHolder {

  public static final List<String> applicationNames = new ArrayList<>(
      Arrays.asList("metis-authentication-rest-test", "metis-core-rest-test"));
  private static final String CONFIGURATION_FILE = "configuration.properties";
  private static final Properties properties = new Properties();
  public static final String APP_NOZZLE_COMMAND_TEMPLATE = "cf app-nozzle %s -filter ContainerMetric";
  public static final int SLEEP_TIME_PER_LOOP_IN_MINS = 1;

  private static String elasticsearchIndexUrl;

  public PropertiesHolder() {
    InputStream input = null;
    try {
      input = new FileInputStream(CONFIGURATION_FILE);
      properties.load(input);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    initializeProperties();
  }

  private void initializeProperties() {
    elasticsearchIndexUrl = (String) properties.get("elasticsearch.index.url");
  }

  public String getElasticsearchIndexUrl() {
    return elasticsearchIndexUrl;
  }
}
