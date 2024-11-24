package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ConstantProvider;

@Documentation(
    identifier = "io.duzzy.plugin.column.constant.BooleanConstantColumn",
    description = "Provide a boolean constant value",
    duzzyType = DuzzyType.PROVIDER,
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
