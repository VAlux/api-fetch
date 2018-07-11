package com.meteogroup.apifetch.fetch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "fetching")
public class FetchingConfigProperties {

  private Integer poolSize;

  private List<FetchingTaskProperties> fetchingTaskProperties;

  public List<FetchingTaskProperties> getFetchingTaskProperties() {
    return fetchingTaskProperties;
  }

  public void setFetchingTaskProperties(List<FetchingTaskProperties> fetchingTaskProperties) {
    this.fetchingTaskProperties = fetchingTaskProperties;
  }

  public Integer getPoolSize() {
    return poolSize;
  }

  public void setPoolSize(Integer poolSize) {
    this.poolSize = poolSize;
  }

  public static class FetchingTaskProperties {
    private String description;
    private String request;
    private String responseRootKey;
    private String directory;
    private String filePrefix;
    private String sourceContentType;
    private String targetContentType;
    private String frequency;
    private String filenameFormat;

    public String getRequest() {
      return request;
    }

    public void setRequest(String request) {
      this.request = request;
    }

    public String getResponseRootKey() {
      return responseRootKey;
    }

    public void setResponseRootKey(String responseRootKey) {
      this.responseRootKey = responseRootKey;
    }

    public String getDirectory() {
      return directory;
    }

    public void setDirectory(String directory) {
      this.directory = directory;
    }

    public String getFilePrefix() {
      return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
      this.filePrefix = filePrefix;
    }

    public String getSourceContentType() {
      return sourceContentType;
    }

    public void setSourceContentType(String sourceContentType) {
      this.sourceContentType = sourceContentType;
    }

    public String getTargetContentType() {
      return targetContentType;
    }

    public void setTargetContentType(String targetContentType) {
      this.targetContentType = targetContentType;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getFrequency() {
      return frequency;
    }

    public void setFrequency(String frequency) {
      this.frequency = frequency;
    }

    public String getFilenameFormat() {
      return filenameFormat;
    }

    public void setFilenameFormat(String filenameFormat) {
      this.filenameFormat = filenameFormat;
    }

    @Override
    public String toString() {
      return "FetchingTaskProperties{" +
          "description='" + description + '\'' +
          ", request='" + request + '\'' +
          ", responseRootKey='" + responseRootKey + '\'' +
          ", directory='" + directory + '\'' +
          ", filePrefix='" + filePrefix + '\'' +
          ", sourceContentType='" + sourceContentType + '\'' +
          ", targetContentType='" + targetContentType + '\'' +
          ", frequency='" + frequency + '\'' +
          ", filenameFormat='" + filenameFormat + '\'' +
          '}';
    }
  }

  @Override
  public String toString() {
    return "FetchingConfigProperties{" +
        "poolSize=" + poolSize +
        ", fetchingTaskProperties=" + fetchingTaskProperties +
        '}';
  }
}
