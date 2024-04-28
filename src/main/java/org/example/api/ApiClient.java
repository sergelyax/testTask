package org.example.api;

import org.example.config.Config;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ApiClient {
  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final Config config;

  public ApiClient() {
    this.httpClient = HttpClients.createDefault();
    this.objectMapper = new ObjectMapper();
    this.config = new Config();
  }

  public List<Map<String, Object>> getPackagesForBranch(String branch) throws IOException {
    String url = config.getApiBaseUrl() + "/export/branch_binary_packages/" + branch;
    HttpGet request = new HttpGet(url);
    HttpResponse response = httpClient.execute(request);
    String jsonResponse = EntityUtils.toString(response.getEntity());

    // Десериализовать JSON с учётом ключа "packages"
    Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<Map<String, Object>>(){});
    return (List<Map<String, Object>>) responseMap.get("packages");
  }


}
