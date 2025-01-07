package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.DuzzyProvider;
import io.duzzy.core.provider.corrupted.LongCorruptedProvider;

@DuzzyProvider
public class LongRandomProvider implements LongCorruptedProvider {

  private final Long min;
  private final Long max;

  public LongRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public LongRandomProvider(
      @JsonProperty("min") Long min,
      @JsonProperty("max") Long max
  ) {
    this.min = min == null ? Long.valueOf(Long.MIN_VALUE) : min;
    this.max = max == null ? Long.valueOf(Long.MAX_VALUE) : max;
  }

  @Override
  public Long value(FieldContext fieldContext) {
    return fieldContext.random().nextLong(this.min, this.max);
  }
}
