package com.tzan.apm.utils;

import java.io.File;
import java.io.FileInputStream;
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
  public static final String CF_LOGIN_COMMAND_TEMPLATE = "cf login -a %s -u %s -p %s -o %s -s %s";
  public static final int SLEEP_TIME_PER_LOOP_IN_MINS = 1;

  private String elasticsearchIndexUrl;
  private String cfApplicationNamesFilePath;
  private String cfApiEndpoint;
  private String cfUsername;
  private String cfPassword;
  private String cfOrganization;
  private String cfTargetSpace;
  private long cfLoginIntervalInMins;

  public PropertiesHolder() {
    InputStream input = null;
    try {
      if (new File(CONFIGURATION_FILE).exists()) { //Used on standalone .jar mode
        input = new FileInputStream(CONFIGURATION_FILE);
        LOGGER.info("Using configuration from file path");
      } else {
        input = getClass().getClassLoader().getResourceAsStream(CONFIGURATION_FILE); //Used on IDE
        LOGGER.info("Using configuration from class path resources directory");
      }
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
    cfApiEndpoint = (String) properties.get("cf.api.endpoint");
    cfUsername = (String) properties.get("cf.username");
    cfPassword = (String) properties.get("cf.password");
    cfOrganization = (String) properties.get("cf.organization");
    cfTargetSpace = (String) properties.get("cf.target.space");
    cfLoginIntervalInMins = Long.parseLong((String) properties.get("cf.login.interval.in.mins"));
  }

  public String getElasticsearchIndexUrl() {
    return elasticsearchIndexUrl;
  }

  public String getCfApplicationNamesFilePath() {
    return cfApplicationNamesFilePath;
  }

  public String getCfApiEndpoint() {
    return cfApiEndpoint;
  }

  public String getCfUsername() {
    return cfUsername;
  }

  public String getCfPassword() {
    return cfPassword;
  }

  public String getCfOrganization() {
    return cfOrganization;
  }

  public String getCfTargetSpace() {
    return cfTargetSpace;
  }

  public long getCfLoginIntervalInMins() {
    return cfLoginIntervalInMins;
  }
}
