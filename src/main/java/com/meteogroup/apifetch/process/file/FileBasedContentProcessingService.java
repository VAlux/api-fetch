package com.meteogroup.apifetch.process.file;

import com.meteogroup.apifetch.fetch.FetchingConfigProperties.FetchingTaskProperties;
import com.meteogroup.apifetch.process.FetchedContentProcessingService;
import com.meteogroup.apifetch.process.FetchedContentTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBasedContentProcessingService implements FetchedContentProcessingService<String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedContentProcessingService.class);

  private final FilenameFormatter<Date> filenameFormatter;
  private final FetchedContentTransformer<String, String> contentFetchedContentTransformer;
  private final FetchingTaskProperties properties;

  public FileBasedContentProcessingService(FilenameFormatter<Date> filenameFormatter,
                                           FetchedContentTransformer<String, String> contentTransformer,
                                           FetchingTaskProperties properties) {
    this.filenameFormatter = filenameFormatter;
    this.contentFetchedContentTransformer = contentTransformer;
    this.properties = properties;
    this.initDirectories();
  }

  private void initDirectories() {
    final String contentPath = properties.getDirectory() + File.separator + properties.getFilePrefix();
    try {
      Files.createDirectories(new File(contentPath).toPath());
    } catch (IOException e) {
      LOGGER.error("Error creating directories: {}", e);
    }
  }

  @Override
  public void process(String content) {
    final Path path = Paths.get(generateFilename());
    final File file = new File(path.toUri());
    try {
      final byte[] transformed = contentFetchedContentTransformer.transform(content).getBytes();
      Files.write(file.toPath(), transformed);
      LOGGER.info("Contents saved to file: {}", file.getPath());
    } catch (IOException e) {
      LOGGER.error("Error writing fetched content to file: {}", e.getMessage());
    }
  }

  private String generateFilename() {
    return Stream.of(
        properties.getDirectory(),
        properties.getFilePrefix(),
        filenameFormatter.format(new Date())
    ).collect(Collectors.joining(File.separator)) + "." + properties.getFileExtension();
  }
}
