package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.FloatCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.FloatConstantProvider",
    description = "Provide a float constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a float"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.FloatConstantProvider"
        value: 42.4
        """
)
public class FloatConstantProvider extends ConstantProvider<Float>
    implements FloatCorruptedProvider {

  @JsonCreator
  public FloatConstantProvider(
      @JsonProperty("value") Float value
  ) {
    super(value);
  }
}
