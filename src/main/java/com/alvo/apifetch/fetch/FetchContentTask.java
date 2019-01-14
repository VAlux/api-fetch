package com.alvo.apifetch.fetch;

import com.alvo.apifetch.fetch.service.FetchService;
import com.alvo.apifetch.process.FetchedContentProcessor;

public class FetchContentTask<T> extends FetchTask<T> {
  private final String requestUrl;

  public FetchContentTask(FetchService<T> fetchService,
                          FetchedContentProcessor<T> processingService,
                          String description,
                          String requestUrl) {
    super(description, fetchService, processingService);
    this.requestUrl = requestUrl;
  }

  @Override
  protected void processFetchedData() {
    fetchService
        .fetch(requestUrl)
        .ifPresent(contentProcessor::process);
  }
}
