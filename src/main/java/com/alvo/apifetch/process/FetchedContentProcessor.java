package com.alvo.apifetch.process;

public interface FetchedContentProcessor<T> {
  void process(T content);
}
