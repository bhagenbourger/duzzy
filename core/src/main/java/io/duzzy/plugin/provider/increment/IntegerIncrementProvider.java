package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.IntegerCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.IntegerIncrementProvider",
    description = "Provide an integer value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
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
        identifier: "io.duzzy.plugin.provider.increment.IntegerIncrementProvider"
        start: 0
        step: 2
        """
)
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
