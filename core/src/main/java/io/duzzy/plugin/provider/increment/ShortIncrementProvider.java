package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.ShortCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.ShortIncrementProvider",
    description = "Provide a short value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to 0",
            defaultValue = "0"
        ),
        @Parameter(
            name = "step",
            description = "The step value, defaults to 1",
            defaultValue = "1"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.ShortIncrementProvider"
        start: 10
        step: 2
        """
)
public class ShortIncrementProvider implements ShortCorruptedProvider {

  private final Short start;
  private final Short step;

  @JsonCreator
  public ShortIncrementProvider(
      @JsonProperty("start") Short start,
      @JsonProperty("step") Short step
  ) {
    this.start = start == null ? Short.valueOf((short) 0) : start;
    this.step = step == null ? Short.valueOf((short) 1) : step;
  }

  @Override
  public Short value(FieldContext fieldContext) {
    return (short) (start + (fieldContext.rowIndex() * step));
  }
}
