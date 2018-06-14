package com.meteogroup.apifetch.process.transform;

import com.meteogroup.apifetch.process.FetchedContentTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContentTransformerFactory {

  private static final Map<ContentTypeRelation, FetchedContentTransformer> TRANSFORMERS_CACHE = new HashMap<>();

  private final List<FetchedContentTransformer> contentTransformers;
  private final ContentTypeRelation defaultTypeRelation;

  @Autowired
  public ContentTransformerFactory(List<FetchedContentTransformer> contentTransformers) {
    this.contentTransformers = contentTransformers;
    defaultTypeRelation = ContentTypeRelation.create(ContentType.UNKNOWN, ContentType.UNKNOWN);
  }

  @PostConstruct
  public void initTransformersCache() {
    contentTransformers.forEach(transformer ->
        TRANSFORMERS_CACHE.put(transformer.getTypeRelation(), transformer));
  }

  public FetchedContentTransformer getTransformer(ContentType source, ContentType target) {
    return TRANSFORMERS_CACHE.getOrDefault(ContentTypeRelation.create(source, target), getDefaultTransformer());
  }

  public FetchedContentTransformer getTransformer(ContentTypeRelation typeRelation) {
    return TRANSFORMERS_CACHE.getOrDefault(typeRelation, getDefaultTransformer());
  }

  public FetchedContentTransformer getTransformer(String sourceContentType, String targetContentType) {
    return TRANSFORMERS_CACHE.getOrDefault(
        ContentTypeRelation.create(sourceContentType, targetContentType),
        getDefaultTransformer());
  }

  private FetchedContentTransformer getDefaultTransformer() {
    return TRANSFORMERS_CACHE.get(defaultTypeRelation);
  }
}
