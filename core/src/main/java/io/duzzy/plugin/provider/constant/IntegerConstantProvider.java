package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.IntegerCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.IntegerConstantProvider",
    description = "Provide an integer constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be an integer"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.IntegerConstantProvider"
        value: 42
        """
)
public class IntegerConstantProvider extends ConstantProvider<Integer>
    implements IntegerCorruptedProvider {

  @JsonCreator
  public IntegerConstantProvider(
      @JsonProperty("value") Integer value
  ) {
    super(value);
  }
}
