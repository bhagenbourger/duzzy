package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;

public class DoubleRandomProvider implements DoubleCorruptedProvider {

  private final Double min;
  private final Double max;

  public DoubleRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public DoubleRandomProvider(
      @JsonProperty("min") Double min,
      @JsonProperty("max") Double max) {
    this.min = min == null ? Double.valueOf(Double.MIN_VALUE) : min;
    this.max = max == null ? Double.valueOf(Double.MAX_VALUE) : max;
  }

  @Override
  public Double value(FieldContext fieldContext) {
    return fieldContext.random().nextDouble(this.min, this.max);
  }
}
