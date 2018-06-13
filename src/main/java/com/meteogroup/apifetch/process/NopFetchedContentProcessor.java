package com.meteogroup.apifetch.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("nopContentProcessingService")
public class NopFetchedContentProcessor implements FetchedContentProcessor<Object> {

  private static final Logger LOGGER = LoggerFactory.getLogger(NopFetchedContentProcessor.class);

  @Override
  public void process(Object content) {
    LOGGER.info("No processing executed on content: {}", content);
  }
}
