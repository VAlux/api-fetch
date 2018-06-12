package com.meteogroup.apifetch.process;

public interface FetchedContentTransformer<I, O> {
  O transform(I input);
}
