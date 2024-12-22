package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;

public class DoubleIncrementProvider implements DoubleCorruptedProvider {

  private final Double start;
  private final Double step;

  @JsonCreator
  public DoubleIncrementProvider(
      @JsonProperty("start") Double start,
      @JsonProperty("step") Double step
  ) {
    this.start = start == null ? Double.valueOf(0d) : start;
    this.step = step == null ? Double.valueOf(0.1d) : step;
  }

  @Override
  public Double value(ColumnContext columnContext) {
    return start + (columnContext.rowIndex().doubleValue() * step);
  }
}
