package com.meteogroup.apifetch.fetch.service;

import com.meteogroup.apifetch.auth.AuthorizationServer;
import com.meteogroup.apifetch.auth.HttpRequestExecutor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetchServiceConfig {

  @Value("${auth.oauth2.serverUrl}")
  private String authServerUrl;

  @Value("${auth.oauth2.clientId}")
  private String authClientId;

  @Value("${auth.oauth2.clientSecret}")
  private String authClientSecret;

  @Bean
  public HttpRequestExecutor requestExecutor() {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    AuthorizationServer authServer = new AuthorizationServer(authServerUrl, authClientId, authClientSecret, httpClient);
    return new HttpRequestExecutor(httpClient, authServer);
  }
}
