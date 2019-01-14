package com.alvo.apifetch.fetch.service;

import com.alvo.apifetch.http.oauth2.AuthorizationServer;
import com.alvo.apifetch.http.oauth2.Oauth2HttpRequestExecutor;
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
  public Oauth2HttpRequestExecutor requestExecutor() {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    AuthorizationServer authServer = new AuthorizationServer(authServerUrl, authClientId, authClientSecret, httpClient);
    return new Oauth2HttpRequestExecutor(httpClient, authServer);
  }
}
