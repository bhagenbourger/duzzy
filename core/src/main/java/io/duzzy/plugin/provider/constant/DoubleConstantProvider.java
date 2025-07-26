package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.DoubleConstantProvider",
    description = "Provide a double constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a double"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.DoubleConstantProvider"
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
