package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.DoubleConstantProvider",
    description = "Provide a double constant value",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "null_rate",
            aliases = {"nullRate", "null-rate"},
            description = "Rate of null values, between 0.0 and 1.0",
            defaultValue = "0.0"
        ),
        @Parameter(
            name = "value",
            description = "The constant value, must be a double"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.column.constant.DoubleConstantProvider"
        null_rate: 0.0
        value: 42.4
        """
)
public class DoubleConstantProvider
    extends ConstantProvider<Double>
    implements DoubleCorruptedProvider {
  @JsonCreator
  public DoubleConstantProvider(
      @JsonProperty("value") Double value
  ) {
    super(value);
  }
}
