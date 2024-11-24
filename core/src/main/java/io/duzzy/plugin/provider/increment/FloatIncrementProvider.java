package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.FloatCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.FloatIncrementProvider",
    description = "Provide a float value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to 0"
        ),
        @Parameter(
            name = "step",
            description = "The step value, defaults to 0.1"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.FloatIncrementProvider"
        start: 0.0
        step: 0.1
        """
)
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
