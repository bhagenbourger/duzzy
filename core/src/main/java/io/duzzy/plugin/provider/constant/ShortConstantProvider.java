package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.ShortCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.ShortConstantProvider",
    description = "Provide a Short constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a Short"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.ShortConstantProvider"
        value: 123
        """
)
public class ShortConstantProvider
    extends ConstantProvider<Short>
    implements ShortCorruptedProvider {
  @JsonCreator
  public ShortConstantProvider(
      @JsonProperty("value") Short value
  ) {
    super(value);
  }
}
