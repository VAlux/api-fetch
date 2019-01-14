package com.alvo.apifetch.process.transform;

import com.alvo.apifetch.process.FetchedContentTransformer;
import com.alvo.apifetch.process.transform.exception.UnsupportedContentStructureException;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Transform json content to csv content.
 * Currently supports only json arrays as a root json content type.
 */
@Component
public class JsonToCsvContentTransformer implements FetchedContentTransformer {

  @Override
  public String transform(String input) throws UnsupportedContentStructureException {
    final JSONObject json = new JSONObject(input);
    for (Object elementName : json.names()) {
      final Object element = json.get((String) elementName);
      if (element instanceof JSONArray) {
        final JSONArray array = ((JSONArray) element);
        return CDL.toString(array);
      }
    }
    throw new UnsupportedContentStructureException("Can parse only json arrays as root elements.");
  }

  @Override
  public ContentType getSourceContentType() {
    return ContentType.JSON;
  }

  @Override
  public ContentType getTargetContentType() {
    return ContentType.CSV;
  }
}
