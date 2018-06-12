package com.meteogroup.apifetch.process.transform;

import com.meteogroup.apifetch.process.FetchedContentTransformer;
import org.springframework.stereotype.Component;

@Component
public class NopFetchedContentTransformer implements FetchedContentTransformer<Object, Object> {
  @Override
  public Object transform(Object input) {
    return input;
  }
}
