package com.meteogroup.apifetch.process;

import com.meteogroup.apifetch.process.transform.ContentType;
import com.meteogroup.apifetch.process.transform.ContentTypeRelation;
import com.meteogroup.apifetch.process.transform.exception.ContentTransformationException;

public interface FetchedContentTransformer {
  String transform(String input) throws ContentTransformationException;

  ContentType getSourceContentType();
  ContentType getTargetContentType();

  default ContentTypeRelation getTypeRelation() {
    return ContentTypeRelation.create(getSourceContentType(), getTargetContentType());
  }
}
