package com.alvo.apifetch.process.transform.exception;

public class ContentTransformationException extends Exception {
  public ContentTransformationException(String message) {
    super("Error occurred during content transformation: " + message);
  }
}
