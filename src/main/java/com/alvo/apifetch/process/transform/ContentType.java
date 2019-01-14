package com.alvo.apifetch.process.transform;

import java.util.Arrays;

public enum ContentType {
  JSON("json"),
  CSV("csv"),
  UNKNOWN("unknown");

  private final String typeName;

  ContentType(String typeName) {
    this.typeName = typeName;
  }

  public String getTypeName() {
    return typeName;
  }

  public static ContentType fromTypeName(String typeName) {
    return Arrays
        .stream(values())
        .filter(contentType -> contentType.typeName.equals(typeName))
        .findFirst()
        .orElse(UNKNOWN);
  }
}
