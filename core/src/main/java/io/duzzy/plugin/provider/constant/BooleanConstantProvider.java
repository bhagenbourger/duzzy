package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.column.constant.BooleanConstantColumn",
    description = "Provide a boolean constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a boolean"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.column.constant.BooleanConstantColumn"
        value: false
        """
)
public class BooleanConstantProvider extends ConstantProvider<Boolean> {

  @JsonCreator
  public BooleanConstantProvider(
      @JsonProperty("value") Boolean value
  ) {
    super(value);
  }

  @Override
  public Boolean corruptedValue(FieldContext fieldContext) {
    return !getConstantValue();
  }
}
