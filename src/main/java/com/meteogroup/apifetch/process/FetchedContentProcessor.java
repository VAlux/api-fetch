package com.meteogroup.apifetch.process;

public interface FetchedContentProcessor<T> {
  void process(T content);
}
