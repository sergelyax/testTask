package org.example.config;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
  private final Properties properties = new Properties();

  public Config() {
    try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
      if (input == null) {
        System.out.println("Sorry, unable to find config.properties");
        return;
      }

      // Загрузить файл свойств
      properties.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public String getApiBaseUrl() {
    return properties.getProperty("api.base.url", "https://rdb.altlinux.org/api");
  }

  public String getApiBranchesEndpoint() {
    return properties.getProperty("api.branches.endpoint", "/export/branch_binary_packages/");
  }
}
