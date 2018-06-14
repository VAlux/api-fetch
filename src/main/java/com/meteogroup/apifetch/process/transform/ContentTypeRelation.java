package com.meteogroup.apifetch.process.transform;

import java.util.Objects;

public final class ContentTypeRelation {

  private final ContentType sourceType;
  private final ContentType targetType;

  private ContentTypeRelation(ContentType sourceType, ContentType targetType) {
    this.sourceType = sourceType;
    this.targetType = targetType;
  }

  public static ContentTypeRelation create(ContentType source, ContentType target) {
    return new ContentTypeRelation(source, target);
  }

  public static ContentTypeRelation create(String source, String target) {
    return new ContentTypeRelation(ContentType.fromTypeName(source), ContentType.fromTypeName(target));
  }

  public ContentType getSourceType() {
    return sourceType;
  }

  public ContentType getTargetType() {
    return targetType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ContentTypeRelation that = (ContentTypeRelation) o;
    return getSourceType() == that.getSourceType()
        && getTargetType() == that.getTargetType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getSourceType(), getTargetType());
  }
}
