package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.DoubleIncrementProvider",
    description = "Provide a double value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, must be a double"
        ),
        @Parameter(
            name = "step",
            description = "The step value, must be a double"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.DoubleIncrementProvider"
        start: 0.0
        step: 0.1
        """
)
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
  public Double value(FieldContext fieldContext) {
    return start + (fieldContext.rowIndex().doubleValue() * step);
  }
}
