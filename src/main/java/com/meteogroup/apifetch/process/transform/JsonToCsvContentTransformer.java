package com.meteogroup.apifetch.process.transform;

import com.meteogroup.apifetch.fetch.FetchingConfigProperties.FetchingTaskProperties;
import com.meteogroup.apifetch.process.FetchedContentTransformer;
import org.json.CDL;
import org.json.JSONObject;

public class JsonToCsvContentTransformer implements FetchedContentTransformer<String, String> {

  private final FetchingTaskProperties properties;

  public JsonToCsvContentTransformer(FetchingTaskProperties properties) {
    this.properties = properties;
  }

  public String transform(String input) {
    final JSONObject json = new JSONObject(input);
    final String responseRootKey = properties.getResponseRootKey();
    if (responseRootKey != null) {
      return CDL.toString(json.getJSONArray(responseRootKey));
    }
    return CDL.toString(json.names());
  }
}
