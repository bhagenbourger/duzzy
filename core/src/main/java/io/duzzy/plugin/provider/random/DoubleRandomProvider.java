package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
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
    this.min = min == null ? Double.MIN_VALUE : min;
    this.max = max == null ? Double.MAX_VALUE : max;
  }

  @Override
  public Double value(ColumnContext columnContext) {
    return columnContext.random().nextDouble(this.min, this.max);
  }
}
