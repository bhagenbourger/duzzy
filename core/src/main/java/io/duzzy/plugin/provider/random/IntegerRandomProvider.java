package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.IntegerCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.IntegerRandomProvider",
    description = "Provide a random integer value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be an integer, inclusive"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be an integer, exclusive"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.IntegerRandomProvider"
        min: 0
        max: 100
        """
)
public class IntegerRandomProvider implements IntegerCorruptedProvider {

  private final Integer min;
  private final Integer max;

  public IntegerRandomProvider() {
    this(null, null);
  }

  public IntegerRandomProvider(
      @JsonProperty("min") Integer min,
      @JsonProperty("max") Integer max) {
    this.min = min == null ? Integer.valueOf(Integer.MIN_VALUE) : min;
    this.max = max == null ? Integer.valueOf(Integer.MAX_VALUE) : max;
  }

  @Override
  public Integer value(FieldContext fieldContext) {
    return fieldContext.random().nextInt(this.min, this.max);
  }
}
