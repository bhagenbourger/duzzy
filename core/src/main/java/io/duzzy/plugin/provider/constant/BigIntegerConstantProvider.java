package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.BigIntegerCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.math.BigInteger;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.BigIntegerConstantProvider",
    description = "Provide a BigInteger constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a BigInteger"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.BigIntegerConstantProvider"
        value: 123456789012345678901234567890
        """
)
public class BigIntegerConstantProvider
    extends ConstantProvider<BigInteger>
    implements BigIntegerCorruptedProvider {
  @JsonCreator
  public BigIntegerConstantProvider(
      @JsonProperty("value") BigInteger value
  ) {
    super(value);
  }
}
