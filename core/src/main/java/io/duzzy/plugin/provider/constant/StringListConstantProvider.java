package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ListConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;
import java.util.List;

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
