package com.alvo.apifetch.http.oauth2;

import com.alvo.apifetch.http.HttpRequestExecutor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Simple http requesting service,
 * which uses provided authorization server to authorize requests.
 */
public class Oauth2HttpRequestExecutor implements HttpRequestExecutor {

  private static final Logger LOGGER = LoggerFactory.getLogger(Oauth2HttpRequestExecutor.class);

  private final CloseableHttpClient httpClient;
  private final ObjectMapper jacksonObjectMapper;
  private final AuthorizationServer authServer;
  private ClientCredentialsResponse token;

  public Oauth2HttpRequestExecutor(CloseableHttpClient httpClient, AuthorizationServer authServer) {
    this.httpClient = httpClient;
    this.authServer = authServer;
    this.jacksonObjectMapper = new ObjectMapper();
  }

  /**
   * Obtain authorization token and execute HTTP request
   *
   * @param request desired {@link HttpRequestBase} to be executed
   * @return obtained {@link CloseableHttpResponse}
   * @throws IOException in case of network, marshalling or error response.
   */
  @Override
  public CloseableHttpResponse execute(HttpRequestBase request) throws IOException {
    LOGGER.info("Executing http request: [{}]", request);
    final String authorizationHeaderValue = String.format("Bearer %s", obtainAccessToken());
    request.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);
    CloseableHttpResponse response = httpClient.execute(request);
    StatusLine statusLine = response.getStatusLine();
    if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
      throw new IOException("fetched with status " + statusLine);
    }
    return response;
  }

  /**
   * Execute with custom response handler
   *
   * @param request         desired {@link HttpRequestBase} to be executed
   * @param responseHandler handler for response processing
   * @param <T>             type of the entity, which will be returned by the specified response handler
   * @return entity, produced by the specified response handler
   * @throws IOException in case of network, marshalling or error response.
   */
  @Override
  public <T> T execute(HttpRequestBase request, ResponseHandler<T> responseHandler) throws IOException {
    LOGGER.info("Executing http request: [{}]", request);
    final String authorizationHeaderValue = String.format("Bearer %s", obtainAccessToken());
    request.setHeader(HttpHeaders.AUTHORIZATION, authorizationHeaderValue);
    return httpClient.execute(request, responseHandler);
  }

  /**
   * Obtain authorization token and execute HTTP request with mapping the response according to the type, <br/>
   * carried with <code>TypeReference</code> parameter.
   *
   * @param request       desired http request base to be executed
   * @param typeReference type information carrier for response mapping.
   * @param <T>           target type for mapping request result.
   * @return mapped response according to type parameter.
   * @throws IOException in case of network, marshalling or error response.
   */
  @Override
  public <T> T execute(HttpRequestBase request, TypeReference<T> typeReference) throws IOException {
    try (CloseableHttpResponse response = execute(request)) {
      return jacksonObjectMapper.readValue(response.getEntity().getContent(), typeReference);
    }
  }

  /**
   * Obtain authorization token and execute HTTP request with mapping the response according to the type, <br/>
   * carried with {@link TypeReference} parameter.
   * If request execution finished with exception - it can be retried for {@code retryAmount} times.
   *
   * @param request       desired http request base to be executed
   * @param typeReference type information carrier for response mapping.
   * @param retryAmount   specified limit of how many times the request execution can be retried to be performed.
   * @param <T>           target type for mapping request result.
   * @return mapped response according to type parameter.
   * @throws IOException in case of network, marshalling or error response.
   */
  @Override
  public <T> T execute(HttpRequestBase request, int retryAmount, TypeReference<T> typeReference) throws IOException {
    try {
      return execute(request, typeReference);
    } catch (IOException ex) {
      if (retryAmount > 0) {
        return execute(request, retryAmount - 1, typeReference);
      } else {
        throw ex;
      }
    }
  }

  /**
   * Obtain authorization token and execute HTTP request with mapping the response according to the type, <br/>
   * carried with {@link TypeReference} parameter.
   * If request execution finished with exception - it can be retried for {@code retryAmount} times.
   *
   * @param request     desired http request base to be executed
   * @param retryAmount specified limit of how many times the request execution can be retried to be performed.
   * @return mapped response according to type parameter.
   * @throws IOException in case of network, marshalling or error response.
   */
  @Override
  public CloseableHttpResponse execute(HttpRequestBase request, int retryAmount) throws IOException {
    try (CloseableHttpResponse response = execute(request)) {
      return response;
    } catch (IOException ex) {
      if (retryAmount > 0) {
        return execute(request, retryAmount - 1);
      } else {
        throw ex;
      }
    }
  }

  private String obtainAccessToken() throws IOException {
    if (token == null || token.getExpiredAt().isBefore(ZonedDateTime.now().plusMinutes(1L))) {
      token = authServer.auth();
    }
    return token.getAccessToken();
  }
}
