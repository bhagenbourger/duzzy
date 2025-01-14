package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.FloatCorruptedProvider;

public class FloatIncrementProvider implements FloatCorruptedProvider {

  private final Float start;
  private final Float step;

  @JsonCreator
  public FloatIncrementProvider(
      @JsonProperty("start") Float start,
      @JsonProperty("step") Float step
  ) {
    this.start = start == null ? Float.valueOf(0f) : start;
    this.step = step == null ? Float.valueOf(0.1f) : step;
  }

  @Override
  public Float value(FieldContext fieldContext) {
    return start + (fieldContext.rowIndex().floatValue() * step);
  }
}
