package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.DoubleRandomProvider",
    description = "Provide a random double value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be a double, inclusive"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be a double, exclusive"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.DoubleRandomProvider"
        min: 0.0
        max: 1.0
        """
)
public class DoubleRandomProvider implements DoubleCorruptedProvider {

  private final Double min;
  private final Double max;

  public DoubleRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public DoubleRandomProvider(
      @JsonProperty("min") Double min,
      @JsonProperty("max") Double max) {
    this.min = min == null ? Double.valueOf(Double.MIN_VALUE) : min;
    this.max = max == null ? Double.valueOf(Double.MAX_VALUE) : max;
  }

  @Override
  public Double value(FieldContext fieldContext) {
    return fieldContext.random().nextDouble(this.min, this.max);
  }
}
