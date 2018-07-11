package com.meteogroup.apifetch.process.file.formatter;

import com.meteogroup.apifetch.process.file.FilenameFormatter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Qualifier(value = "nopFormatter")
public class NopFilenameFormatter implements FilenameFormatter {

  @Override
  public Optional<String> format(String input) {
    return Optional.of(input);
  }

  @Override
  public boolean isApplicable(String input) {
    return false;
  }
}
