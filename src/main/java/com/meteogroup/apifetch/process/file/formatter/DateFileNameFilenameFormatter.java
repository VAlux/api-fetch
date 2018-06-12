package com.meteogroup.apifetch.process.file.formatter;

import com.meteogroup.apifetch.process.file.FilenameFormatter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateFileNameFilenameFormatter implements FilenameFormatter<Date> {

  private static final String FILE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ssZZ";

  private final DateFormat formatter;

  public DateFileNameFilenameFormatter() {
    this.formatter = new SimpleDateFormat(FILE_NAME_FORMAT);
  }

  @Override
  public String format(Date date) {
    return formatter.format(date);
  }
}
