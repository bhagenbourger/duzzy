package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.provider.corrupted.BigDecimalCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.math.BigDecimal;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.BigDecimalRandomProvider",
    description = "Provide a random big decimal value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be a big decimal, inclusive",
            defaultValue = "BigDecimal.valueOf(Double.MIN_VALUE)"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be a big decimal, exclusive",
            defaultValue = "BigDecimal.valueOf(Double.MAX_VALUE)"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.BigDecimalRandomProvider"
        min: 0.0
        max: 1.0
        """
)
public final class BigDecimalRandomProvider implements BigDecimalCorruptedProvider {

  private final BigDecimal min;
  private final BigDecimal max;

  public BigDecimalRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public BigDecimalRandomProvider(
      @JsonProperty("min") BigDecimal min,
      @JsonProperty("max") BigDecimal max) {
    this.min = min == null ? BigDecimal.valueOf(Double.MIN_VALUE) : min;
    this.max = max == null ? BigDecimal.valueOf(Double.MAX_VALUE) : max;
    assert this.min.compareTo(this.max) < 0 : "Min big decimal must be before max big decimal";
  }

  @Override
  public BigDecimal value(FieldContext fieldContext) {
    final String random = String.valueOf(fieldContext.random().nextDouble());
    final BigDecimal range = max.subtract(min);
    final BigDecimal randomValue = range.multiply(new BigDecimal(random));
    return min.add(randomValue);
  }
}
