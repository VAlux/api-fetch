package com.meteogroup.apifetch.fetch;

import com.meteogroup.apifetch.fetch.service.FetchService;
import com.meteogroup.apifetch.process.FetchedContentProcessingService;

public abstract class FetchTask<T> implements Runnable {

  protected final FetchService<T> fetchService;
  protected final FetchedContentProcessingService<T> processingService;

  protected FetchTask(FetchService<T> fetchService, FetchedContentProcessingService<T> processingService) {
    this.fetchService = fetchService;
    this.processingService = processingService;
  }

  @Override
  public void run() {
    processFetchedData();
  }

  protected abstract void processFetchedData();
}
