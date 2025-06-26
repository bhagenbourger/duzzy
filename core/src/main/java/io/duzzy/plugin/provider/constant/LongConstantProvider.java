package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.LongCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.LongConstantProvider",
    description = "Provide a long constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a long"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.LongConstantProvider"
        value: 42
        """
)
public class LongConstantProvider extends ConstantProvider<Long> implements LongCorruptedProvider {

  @JsonCreator
  public LongConstantProvider(
      @JsonProperty("value") Long value
  ) {
    super(value);
  }
}
