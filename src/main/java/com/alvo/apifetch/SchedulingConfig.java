package com.alvo.apifetch;

import com.alvo.apifetch.process.transform.ContentTransformerFactory;
import com.alvo.apifetch.fetch.FetchContentTask;
import com.alvo.apifetch.fetch.FetchingConfigProperties;
import com.alvo.apifetch.fetch.FetchingConfigProperties.FetchingTaskProperties;
import com.alvo.apifetch.fetch.service.FetchService;
import com.alvo.apifetch.process.FetchedContentTransformer;
import com.alvo.apifetch.process.file.FileBasedContentProcessor;
import com.alvo.apifetch.process.file.FilenameFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.List;

@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

  private static final String FETCHING_TASKS_POOL_NAME = "fetching-tasks-pool-";

  private final FetchingConfigProperties properties;
  private final FetchService<String> fetchService;
  private final List<FilenameFormatter> filenameFormatters;
  private final FilenameFormatter nopFilenameFormatter;
  private final ContentTransformerFactory transformerFactory;

  @Autowired
  public SchedulingConfig(FetchingConfigProperties properties,
                          FetchService<String> fetchService,
                          List<FilenameFormatter> filenameFormatters,
                          ContentTransformerFactory transformerFactory,
                          @Qualifier("nopFormatter") FilenameFormatter nopFilenameFormatter) {
    this.properties = properties;
    this.fetchService = fetchService;
    this.filenameFormatters = filenameFormatters;
    this.nopFilenameFormatter = nopFilenameFormatter;
    this.transformerFactory = transformerFactory;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
    final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    threadPoolTaskScheduler.setPoolSize(properties.getPoolSize());
    threadPoolTaskScheduler.setThreadNamePrefix(FETCHING_TASKS_POOL_NAME);
    threadPoolTaskScheduler.initialize();

    scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

    properties.getFetchingTaskProperties().stream()
        .map(this::createTask)
        .forEach(scheduledTaskRegistrar::addCronTask);
  }

  private CronTask createTask(FetchingTaskProperties taskProperties) {
    FetchedContentTransformer contentTransformer =
        transformerFactory.getTransformer(
            taskProperties.getSourceContentType(),
            taskProperties.getTargetContentType());

    FilenameFormatter formatter = inferFilenameFormatter(taskProperties.getFilenameFormat());

    FileBasedContentProcessor processingService =
        new FileBasedContentProcessor(formatter, contentTransformer, taskProperties);

    FetchContentTask<String> fetchContentTask =
        new FetchContentTask<>(
            fetchService,
            processingService,
            taskProperties.getDescription(),
            taskProperties.getRequest());

    return new CronTask(fetchContentTask, taskProperties.getFrequency());
  }

  private FilenameFormatter inferFilenameFormatter(final String filenameFormat) {
    return filenameFormatters.stream()
        .filter(formatter -> formatter.isApplicable(filenameFormat))
        .findAny()
        .orElse(nopFilenameFormatter);
  }
}
