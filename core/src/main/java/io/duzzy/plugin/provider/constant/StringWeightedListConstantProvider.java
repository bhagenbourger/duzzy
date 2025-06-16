package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.WeightedItem;
import io.duzzy.core.provider.constant.WeightedListConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.util.List;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.StringWeightedListConstantProvider",
    description = "Provide a weighted list of string constant values",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "values",
            description = "The constant values, must be a list of weighted strings"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.StringWeightedListConstantProvider"
        values:
          - value: "one"
            weight: 1
          - value: "two"
            weight: 3
          - value: "three"
            weight: 2
        """
)
public class StringWeightedListConstantProvider
    extends WeightedListConstantProvider<String>
    implements StringCorruptedProvider {

  private static final List<WeightedItem<String>> DEFAULT =
      List.of(new WeightedItem<>("constant", 1));

  @JsonCreator
  public StringWeightedListConstantProvider(
      @JsonProperty("values") List<WeightedItem<String>> values
  ) {
    super(values == null || values.isEmpty() ? DEFAULT : values);
  }

  @Override
  public String corruptedValue(FieldContext fieldContext) {
    return StringCorruptedProvider.corruptedValue(fieldContext,
        getValues().getFirst().value().length());
  }
}
