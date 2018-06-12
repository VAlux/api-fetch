package com.meteogroup.apifetch.process.file.formatter;

import com.meteogroup.apifetch.process.file.FilenameFormatter;
import org.springframework.stereotype.Component;

@Component
public class NopFileNameFilenameFormatter implements FilenameFormatter<Object> {
  @Override
  public String format(Object input) {
    return input.toString();
  }
}
