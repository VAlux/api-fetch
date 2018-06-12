package com.meteogroup.apifetch.process;

public interface FetchedContentProcessingService<T> {
  void process(T content);
}
