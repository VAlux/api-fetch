package com.meteogroup.apifetch.fetch.service;

import java.util.Optional;

public interface FetchService<T> {
  Optional<T> fetch(String requestUrl);
}
