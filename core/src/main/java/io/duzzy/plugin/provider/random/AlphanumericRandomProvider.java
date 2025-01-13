package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.ProviderUtil;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;

public class AlphanumericRandomProvider implements StringCorruptedProvider {

  private final int minLength;
  private final int maxLength;

  public AlphanumericRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public AlphanumericRandomProvider(
      @JsonProperty("min_length") @JsonAlias({"minLength", "min-length"}) Integer minLength,
      @JsonProperty("max_length") @JsonAlias({"maxLength", "max-length"}) Integer maxLength
  ) {
    this.minLength = minLength == null ? 10 : minLength;
    this.maxLength = maxLength == null ? 15 : maxLength;
  }

  @Override
  public String value(FieldContext fieldContext) {
    int length = fieldContext.random().nextInt(minLength, maxLength + 1);
    return ProviderUtil.randomString(fieldContext.random(), length);
  }

  @Override
  public String corruptedValue(FieldContext fieldContext) {
    return StringCorruptedProvider.corruptedValue(fieldContext, maxLength);
  }
}
