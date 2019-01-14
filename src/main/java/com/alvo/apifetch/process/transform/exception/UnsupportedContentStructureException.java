package com.alvo.apifetch.process.transform.exception;

public class UnsupportedContentStructureException extends ContentTransformationException {
  public UnsupportedContentStructureException(String message) {
    super("Unsupported content structure: " + message);
  }
}
