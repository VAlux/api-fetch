package com.alvo.apifetch.http;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

public interface HttpRequestExecutor {
  CloseableHttpResponse execute(HttpRequestBase request) throws IOException;

  <T> T execute(HttpRequestBase request, ResponseHandler<T> responseHandler) throws IOException;

  <T> T execute(HttpRequestBase request, TypeReference<T> typeReference) throws IOException;

  <T> T execute(HttpRequestBase request, int retryAmount, TypeReference<T> typeReference) throws IOException;

  CloseableHttpResponse execute(HttpRequestBase request, int retryAmount) throws IOException;
}
