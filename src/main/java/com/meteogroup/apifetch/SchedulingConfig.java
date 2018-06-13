package com.meteogroup.apifetch;

import com.meteogroup.apifetch.fetch.FetchContentTask;
import com.meteogroup.apifetch.fetch.FetchingConfigProperties;
import com.meteogroup.apifetch.fetch.FetchingConfigProperties.FetchingTaskProperties;
import com.meteogroup.apifetch.fetch.service.FetchService;
import com.meteogroup.apifetch.process.file.FileBasedContentProcessor;
import com.meteogroup.apifetch.process.file.FilenameFormatter;
import com.meteogroup.apifetch.process.transform.JsonToCsvContentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SchedulingConfig implements SchedulingConfigurer {

  private static final int POOL_SIZE = 10;

  private final FetchingConfigProperties properties;
  private final FetchService<String> fetchService;
  private final FilenameFormatter<Date> filenameFormatter;

  @Autowired
  public SchedulingConfig(FetchingConfigProperties properties,
                          FetchService<String> fetchService,
                          FilenameFormatter<Date> filenameFormatter) {
    this.properties = properties;
    this.fetchService = fetchService;
    this.filenameFormatter = filenameFormatter;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
    final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
    threadPoolTaskScheduler.setThreadNamePrefix("fetching-task-pool-");
    threadPoolTaskScheduler.initialize();

    scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);

    final List<CronTask> tasks = properties.getFetchingTaskProperties().stream()
        .map(this::createTask)
        .collect(Collectors.toList());

    tasks.forEach(scheduledTaskRegistrar::addCronTask);
  }

  private CronTask createTask(FetchingTaskProperties taskProperties) {
    final JsonToCsvContentTransformer contentTransformer =
        new JsonToCsvContentTransformer(taskProperties);

    final FileBasedContentProcessor processingService =
        new FileBasedContentProcessor(filenameFormatter, contentTransformer, taskProperties);

    final FetchContentTask<String> fetchContentTask =
        new FetchContentTask<>(fetchService, processingService, taskProperties.getRequest());

    return new CronTask(fetchContentTask, taskProperties.getFrequency());
  }
}
