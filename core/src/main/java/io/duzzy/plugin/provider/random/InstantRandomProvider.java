package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.InstantCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.Instant;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.InstantRandomProvider",
    description = "Provide a random instant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = """
                The minimum instant value, \
                must be a string that represents a valid instant, inclusive"""
        ),
        @Parameter(
            name = "max",
            description = """
                The maximum instant value, \
                must be a string that represents a valid instant, exclusive"""
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.InstantRandomProvider"
        min: "2020-01-01T00:00:00Z"
        max: "2021-01-01T00:00:00Z"
        """
)
public final class InstantRandomProvider implements InstantCorruptedProvider {

  private static final Long DEFAULT_MAX = 253402300800000L; //9999-12-31 + 1 day

  private final Long min;
  private final Long max;

  public InstantRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public InstantRandomProvider(
      @JsonProperty("min") String min,
      @JsonProperty("max") String max
  ) {
    this.min = min == null ? 0 : Instant.parse(min).toEpochMilli();
    this.max = max == null ? DEFAULT_MAX : Instant.parse(max).toEpochMilli();
    assert this.min < this.max : "Min instant must be before max instant";
  }

  @Override
  public Instant value(FieldContext fieldContext) {
    return Instant.ofEpochMilli(fieldContext.random().nextLong(this.min, this.max));
  }
}
