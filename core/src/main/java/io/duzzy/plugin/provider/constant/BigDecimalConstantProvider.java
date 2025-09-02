package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.BigDecimalCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.math.BigDecimal;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.BigDecimalConstantProvider",
    description = "Provide a big decimal constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a big decimal"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.BigDecimalConstantProvider"
        value: 42.4
        """
)
public class BigDecimalConstantProvider
    extends ConstantProvider<BigDecimal>
    implements BigDecimalCorruptedProvider {
  @JsonCreator
  public BigDecimalConstantProvider(
      @JsonProperty("value") BigDecimal value
  ) {
    super(value);
  }
}
