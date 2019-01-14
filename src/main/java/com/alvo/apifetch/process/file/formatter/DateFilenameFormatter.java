package com.alvo.apifetch.process.file.formatter;

import com.alvo.apifetch.process.file.FilenameFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
public class DateFilenameFormatter implements FilenameFormatter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DateFilenameFormatter.class);

  private static final char PATTERN_BEGIN_INDICATOR = '{';
  private static final char PATTERN_END_INDICATOR = '}';
  private final SimpleDateFormat formatter;

  public DateFilenameFormatter() {
    this.formatter = new SimpleDateFormat();
  }

  @Override
  public Optional<String> format(final String pattern) {
    return extractDatePatternPart(pattern, false)
        .map(this::formatDate)
        .flatMap(formattedDate -> replacePattern(formattedDate, pattern));
  }

  private Optional<String> replacePattern(final String formattedDate, final String pattern) {
    return extractDatePatternPart(pattern, true)
        .map(extractedPattern -> pattern.replace(extractedPattern, formattedDate));
  }

  private String formatDate(final String datePattern) {
    formatter.applyPattern(datePattern);
    return formatter.format(new Date());
  }

  private Optional<String> extractDatePatternPart(final String input, boolean includeIndicators) {
    final int offset = includeIndicators ? 1 : 0;
    final int patternBeginIndex = input.indexOf(PATTERN_BEGIN_INDICATOR) + 1;
    final int patternEndIndex = input.indexOf(PATTERN_END_INDICATOR);
    if (patternBeginIndex >= 0 && patternEndIndex >= 0) {
      return Optional.of(input.substring(patternBeginIndex - offset, patternEndIndex + offset));
    }

    return Optional.empty();
  }

  @Override
  public boolean isApplicable(final String input) {
    if (input == null || input.length() <= 0) {
      return false;
    }

    try {
      extractDatePatternPart(input, false).ifPresent(formatter::applyPattern);
    } catch (IllegalArgumentException e) {
      LOGGER.error("Formatter is not applicable for specified input: {}", input, e);
      return false;
    }

    return true;
  }
}
