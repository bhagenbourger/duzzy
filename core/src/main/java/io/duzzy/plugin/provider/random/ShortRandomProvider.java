package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.ShortCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.ShortRandomProvider",
    description = "Provide a random short value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be a short, inclusive",
            defaultValue = "-32768"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be a short, exclusive",
            defaultValue = "32767"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.ShortRandomProvider"
        min: 0
        max: 100
        """
)
public class ShortRandomProvider implements ShortCorruptedProvider {

  private final Short min;
  private final Short max;

  public ShortRandomProvider() {
    this(null, null);
  }

  public ShortRandomProvider(
      @JsonProperty("min") Short min,
      @JsonProperty("max") Short max) {
    this.min = min == null ? Short.valueOf(Short.MIN_VALUE) : min;
    this.max = max == null ? Short.valueOf(Short.MAX_VALUE) : max;
  }

  @Override
  public Short value(FieldContext fieldContext) {
    return (short) fieldContext.random().nextInt(this.min, this.max);
  }
}
