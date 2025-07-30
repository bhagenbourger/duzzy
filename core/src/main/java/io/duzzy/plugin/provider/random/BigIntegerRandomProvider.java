package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.BigIntegerCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.math.BigInteger;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.BigIntegerRandomProvider",
    description = "Provide a random big integer value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum value, must be a big integer, inclusive",
            defaultValue = "BigInteger.valueOf(Long.MIN_VALUE)"
        ),
        @Parameter(
            name = "max",
            description = "The maximum value, must be a big integer, exclusive",
            defaultValue = "BigInteger.valueOf(Long.MAX_VALUE)"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.BigIntegerRandomProvider"
        min: 0
        max: 100
        """
)
public final class BigIntegerRandomProvider implements BigIntegerCorruptedProvider {

  private final BigInteger min;
  private final BigInteger max;

  public BigIntegerRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public BigIntegerRandomProvider(
      @JsonProperty("min") BigInteger min,
      @JsonProperty("max") BigInteger max) {
    this.min = min == null ? BigInteger.valueOf(Long.MIN_VALUE) : min;
    this.max = max == null ? BigInteger.valueOf(Long.MAX_VALUE) : max;
    assert this.min.compareTo(this.max) < 0 : "Min big integer must be before max big integer";
  }

  @Override
  public BigInteger value(FieldContext fieldContext) {
    final BigInteger random = new BigInteger(min.bitLength(), fieldContext.random());
    final BigInteger range = max.subtract(min);
    if (random.compareTo(min) < 0) {
      return random.add(min);
    } else if (random.compareTo(max) > 0) {
      return random.mod(range).add(min);
    } else {
      return random;
    }
  }
}
