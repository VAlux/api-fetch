package com.meteogroup.apifetch.process.transform;

import com.meteogroup.apifetch.process.FetchedContentTransformer;
import org.springframework.stereotype.Component;

@Component
public class NopContentTransformer implements FetchedContentTransformer {

  @Override
  public String transform(String input) {
    return input;
  }

  @Override
  public ContentType getSourceContentType() {
    return ContentType.UNKNOWN;
  }

  @Override
  public ContentType getTargetContentType() {
    return ContentType.UNKNOWN;
  }
}
