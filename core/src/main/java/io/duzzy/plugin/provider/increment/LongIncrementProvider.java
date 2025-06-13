package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.LongCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.LongIncrementProvider",
    description = "Provide a long value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to 0"
        ),
        @Parameter(
            name = "step",
            description = "The step value, defaults to 1"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.LongIncrementProvider"
        start: 0
        step: 2
        """
)
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
