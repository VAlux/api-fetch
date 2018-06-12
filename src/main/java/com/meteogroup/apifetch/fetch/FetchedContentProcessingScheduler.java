package com.meteogroup.apifetch.fetch;

import com.meteogroup.apifetch.fetch.service.FetchService;
import com.meteogroup.apifetch.process.FetchedContentProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FetchedContentProcessingScheduler {

  private final FetchService<String> fetchService;
  private final FetchedContentProcessingService<String> processingService;
  private final FetchingConfigProperties properties;

  @Autowired
  public FetchedContentProcessingScheduler(FetchService<String> fetchService,
                                           FetchedContentProcessingService<String> processingService,
                                           FetchingConfigProperties properties) {
    this.fetchService = fetchService;
    this.processingService = processingService;
    this.properties = properties;
  }

  @Scheduled(cron = "${fetching.frequency}")
  public void processFetchedData() {
    fetchService.fetch(properties.getRequest()).ifPresent(processingService::process);
  }
}
