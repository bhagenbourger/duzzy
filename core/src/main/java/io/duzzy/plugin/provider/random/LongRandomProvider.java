package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.DuzzyProvider;
import io.duzzy.core.provider.corrupted.LongCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@DuzzyProvider
@Documentation(
    identifier = "io.duzzy.plugin.provider.random.LongRandomProvider",
    description = "Provide a random long value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be a long, inclusive"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be a long, exclusive"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.LongRandomProvider"
        min: 0
        max: 100
        """
)
public class LongRandomProvider implements LongCorruptedProvider {

  private final Long min;
  private final Long max;

  public LongRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public LongRandomProvider(
      @JsonProperty("min") Long min,
      @JsonProperty("max") Long max
  ) {
    this.min = min == null ? Long.valueOf(Long.MIN_VALUE) : min;
    this.max = max == null ? Long.valueOf(Long.MAX_VALUE) : max;
  }

  @Override
  public Long value(FieldContext fieldContext) {
    return fieldContext.random().nextLong(this.min, this.max);
  }
}
