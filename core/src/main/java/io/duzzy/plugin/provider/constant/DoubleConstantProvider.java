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
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a double"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.column.constant.DoubleConstantProvider"
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
