package com.meteogroup.apifetch.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.stream.Collectors;

public class AuthorizationServer {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationServer.class);

  private final String accessTokenUrl;
  private final String clientId;
  private final String clientSecret;
  private final CloseableHttpClient httpClient;
  private final ObjectMapper mapper;

  public AuthorizationServer(String accessTokenUrl,
                             String clientId,
                             String clientSecret,
                             CloseableHttpClient httpClient) {
    this.accessTokenUrl = accessTokenUrl;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.httpClient = httpClient;
    this.mapper = new ObjectMapper();
  }

  public ClientCredentialsResponse auth() throws IOException {
    HttpPost authRequest = new HttpPost(accessTokenUrl);
    LOGGER.info("Performing authentication request: [{}]", authRequest);
    authRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " +
        new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes())));

    authRequest.setEntity(new StringEntity("grant_type=client_credentials", ContentType.APPLICATION_FORM_URLENCODED));

    try (CloseableHttpResponse response = httpClient.execute(authRequest)) {
      LOGGER.info("Obtained authentication response: [{}]", response);
      HttpEntity entity = response.getEntity();

      BufferedReader contentStreamReader = new BufferedReader(new InputStreamReader(entity.getContent()));

      final String content = contentStreamReader
          .lines()
          .collect(Collectors.joining(System.lineSeparator()));

      return mapper.readValue(content, ClientCredentialsResponse.class);
    }
  }
}
