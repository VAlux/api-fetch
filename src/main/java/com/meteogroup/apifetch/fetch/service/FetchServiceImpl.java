package com.meteogroup.apifetch.fetch.service;

import com.meteogroup.apifetch.http.oauth2.Oauth2HttpRequestExecutor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

@Service
public class FetchServiceImpl implements FetchService<String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FetchServiceImpl.class);

  private final Oauth2HttpRequestExecutor executor;

  @Autowired
  public FetchServiceImpl(Oauth2HttpRequestExecutor executor) {
    this.executor = executor;
  }

  @Override
  public Optional<String> fetch(String requestUrl) {
    final HttpGet httpGet = new HttpGet(requestUrl);
    try (CloseableHttpResponse response = executor.execute(httpGet)) {
      return Optional.of(StreamUtils.copyToString(response.getEntity().getContent(), Charset.defaultCharset()));
    } catch (IOException e) {
      LOGGER.error("Cant fetch data: {} URL: {}", e.getMessage(), requestUrl);
    }
    return Optional.empty();
  }
}
