package com.meteogroup.apifetch.process.transform;

import com.meteogroup.apifetch.fetch.FetchingConfigProperties;
import com.meteogroup.apifetch.process.FetchedContentTransformer;
import org.json.CDL;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("jsonToCsvContentTransformer")
public class JsonToCsvContentTransformer implements FetchedContentTransformer<String, String> {

  private final FetchingConfigProperties properties;

  @Autowired
  public JsonToCsvContentTransformer(FetchingConfigProperties properties) {
    this.properties = properties;
  }

  @Override
  public String transform(String input) {
    final JSONObject json = new JSONObject(input);
    final String responseRootKey = properties.getResponseRootKey();
    if (responseRootKey != null) {
      return CDL.toString(json.getJSONArray(responseRootKey));
    }
    return CDL.toString(json.names());
  }
}
