package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.LongCorruptedProvider;

public class LongIncrementProvider implements LongCorruptedProvider {

  private final Long start;
  private final Long step;

  @JsonCreator
  public LongIncrementProvider(
      @JsonProperty("start") Long start,
      @JsonProperty("step") Long step
  ) {
    this.start = start == null ? Long.valueOf(0L) : start;
    this.step = step == null ? Long.valueOf(1L) : step;
  }

  @Override
  public Long value(FieldContext fieldContext) {
    return start + (fieldContext.rowIndex() * step);
  }
}
