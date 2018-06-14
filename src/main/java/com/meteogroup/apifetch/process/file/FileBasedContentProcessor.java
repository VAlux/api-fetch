package com.meteogroup.apifetch.process.file;

import com.meteogroup.apifetch.fetch.FetchingConfigProperties.FetchingTaskProperties;
import com.meteogroup.apifetch.process.FetchedContentProcessor;
import com.meteogroup.apifetch.process.FetchedContentTransformer;
import com.meteogroup.apifetch.process.transform.exception.ContentTransformationException;
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

public class FileBasedContentProcessor implements FetchedContentProcessor<String> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedContentProcessor.class);

  private final FilenameFormatter<Date> filenameFormatter;
  private final FetchedContentTransformer contentFetchedContentTransformer;
  private final FetchingTaskProperties properties;

  public FileBasedContentProcessor(FilenameFormatter<Date> filenameFormatter,
                                   FetchedContentTransformer contentTransformer,
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
      LOGGER.error("Error writing fetched content to file: {}", e);
    } catch (ContentTransformationException e) {
      LOGGER.error("Error occurred during fetched content transformation: {}", e);
    }
  }

  private String generateFilename() {
    return Stream.of(
        properties.getDirectory(),
        properties.getFilePrefix(),
        filenameFormatter.format(new Date())
    ).collect(Collectors.joining(File.separator)) + "." + properties.getTargetContentType();
  }
}
