package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.FloatCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.FloatRandomProvider",
    description = "Provide a random float value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be a float, inclusive"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be a float, exclusive"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.FloatRandomProvider"
        min: 0.0
        max: 1.0
        """
)
public class FloatRandomProvider implements FloatCorruptedProvider {

  private final Float min;
  private final Float max;

  public FloatRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public FloatRandomProvider(
      @JsonProperty("min") Float min,
      @JsonProperty("max") Float max
  ) {
    this.min = min == null ? Float.valueOf(Float.MIN_VALUE) : min;
    this.max = max == null ? Float.valueOf(Float.MAX_VALUE) : max;
  }

  @Override
  public Float value(FieldContext fieldContext) {
    return fieldContext.random().nextFloat(this.min, this.max);
  }
}
