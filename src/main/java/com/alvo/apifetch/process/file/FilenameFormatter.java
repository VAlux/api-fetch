package com.alvo.apifetch.process.file;

import java.util.Optional;

public interface FilenameFormatter {
  Optional<String> format(String pattern);

  boolean isApplicable(String input);
}
