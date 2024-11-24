package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.StringConstantProvider",
    description = "Provide a string constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a string"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.StringConstantProvider"
        value: "constant"
        """
)
public class StringConstantProvider extends ConstantProvider<String>
    implements StringCorruptedProvider {

  private static final String DEFAULT_VALUE = "constant";

  @JsonCreator
  public StringConstantProvider(
      @JsonProperty("value") String value
  ) {
    super(value == null ? DEFAULT_VALUE : value);
  }

  @Override
  public String corruptedValue(FieldContext fieldContext) {
    return StringCorruptedProvider.corruptedValue(fieldContext, getConstantValue().length());
  }
}
