package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.BigIntegerCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.math.BigInteger;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.BigIntegerIncrementProvider",
    description = "Provide a big integer value that increments by a step",
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
        identifier: "io.duzzy.plugin.provider.increment.BigIntegerIncrementProvider"
        start: 10
        step: 2
        """
)
public class BigIntegerIncrementProvider implements BigIntegerCorruptedProvider {

  private final BigInteger start;
  private final BigInteger step;

  @JsonCreator
  public BigIntegerIncrementProvider(
      @JsonProperty("start") BigInteger start,
      @JsonProperty("step") BigInteger step
  ) {
    this.start = start == null ? BigInteger.ZERO : start;
    this.step = step == null ? BigInteger.ONE : step;
  }

  @Override
  public BigInteger value(FieldContext fieldContext) {
    return start.add(BigInteger.valueOf(fieldContext.rowIndex()).multiply(step));
  }
}
