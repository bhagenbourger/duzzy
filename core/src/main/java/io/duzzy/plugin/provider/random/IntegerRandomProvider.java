package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.corrupted.IntegerCorruptedProvider;

public class IntegerRandomProvider implements IntegerCorruptedProvider {

  private final Integer min;
  private final Integer max;

  public IntegerRandomProvider() {
    this(null, null);
  }

  public IntegerRandomProvider(
      @JsonProperty("min") Integer min,
      @JsonProperty("max") Integer max) {
    this.min = min == null ? Integer.valueOf(Integer.MIN_VALUE) : min;
    this.max = max == null ? Integer.valueOf(Integer.MAX_VALUE) : max;
  }

  @Override
  public Integer value(ColumnContext columnContext) {
    return columnContext.random().nextInt(this.min, this.max);
  }
}
