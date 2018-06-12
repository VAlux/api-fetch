package com.meteogroup.apifetch.process.file;

public interface FilenameFormatter<T> {
  String format(T input);
}
