package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ListConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;
import java.util.List;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.StringListConstantProvider",
    description = "Provide a list of string constant values",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    parameters = {
        @Parameter(
            name = "values",
            description = "The constant values, must be a list of strings"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.StringListConstantProvider"
        values:
          - "constant1"
          - "constant2"
        """
)
public class StringListConstantProvider extends ListConstantProvider<String>
    implements StringCorruptedProvider {

  @JsonCreator
  public StringListConstantProvider(
      @JsonProperty("values") List<String> values
  ) {
    super(values == null || values.isEmpty() ? List.of("constant") : values);
  }

  @Override
  public String corruptedValue(FieldContext fieldContext) {
    return StringCorruptedProvider.corruptedValue(fieldContext, getValues().getFirst().length());
  }
}
