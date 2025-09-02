package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.BigDecimalCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.math.BigDecimal;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.BigDecimalIncrementProvider",
    description = "Provide a big decimal value that increments by a step",
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
        identifier: "io.duzzy.plugin.provider.increment.BigDecimalIncrementProvider"
        start: 10.5
        step: 2.5
        """
)
public class BigDecimalIncrementProvider implements BigDecimalCorruptedProvider {

  private final BigDecimal start;
  private final BigDecimal step;

  @JsonCreator
  public BigDecimalIncrementProvider(
      @JsonProperty("start") BigDecimal start,
      @JsonProperty("step") BigDecimal step
  ) {
    this.start = start == null ? BigDecimal.ZERO : start;
    this.step = step == null ? BigDecimal.ONE : step;
  }

  @Override
  public BigDecimal value(FieldContext fieldContext) {
    return start.add(BigDecimal.valueOf(fieldContext.rowIndex()).multiply(step));
  }
}
