package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.WeightedItem;
import io.duzzy.core.provider.constant.WeightedListConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;
import java.util.List;

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
