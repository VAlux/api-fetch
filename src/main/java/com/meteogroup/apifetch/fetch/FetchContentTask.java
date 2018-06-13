package com.meteogroup.apifetch.fetch;

import com.meteogroup.apifetch.fetch.service.FetchService;
import com.meteogroup.apifetch.process.FetchedContentProcessingService;

public class FetchContentTask<T> extends FetchTask<T> {

  private final String requestUrl;

  public FetchContentTask(FetchService<T> fetchService,
                             FetchedContentProcessingService<T> processingService,
                             String requestUrl) {
    super(fetchService, processingService);
    this.requestUrl = requestUrl;
  }

  @Override
  protected void processFetchedData() {
    fetchService
        .fetch(requestUrl)
        .ifPresent(processingService::process);
  }
}
