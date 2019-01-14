package com.alvo.apifetch.process;

import com.alvo.apifetch.process.transform.ContentType;
import com.alvo.apifetch.process.transform.ContentTypeRelation;
import com.alvo.apifetch.process.transform.exception.ContentTransformationException;

public interface FetchedContentTransformer {
  String transform(String input) throws ContentTransformationException;

  ContentType getSourceContentType();
  ContentType getTargetContentType();

  default ContentTypeRelation getTypeRelation() {
    return ContentTypeRelation.create(getSourceContentType(), getTargetContentType());
  }
}
