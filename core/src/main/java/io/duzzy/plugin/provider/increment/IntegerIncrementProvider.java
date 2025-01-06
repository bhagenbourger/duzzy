package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.IntegerCorruptedProvider;

public class IntegerIncrementProvider implements IntegerCorruptedProvider {

  private final Integer start;
  private final Integer step;

  @JsonCreator
  public IntegerIncrementProvider(
      @JsonProperty("start") Integer start,
      @JsonProperty("step") Integer step
  ) {
    this.start = start == null ? Integer.valueOf(0) : start;
    this.step = step == null ? Integer.valueOf(1) : step;
  }

  @Override
  public Integer value(FieldContext fieldContext) {
    return start + (fieldContext.rowIndex().intValue() * step);
  }
}
