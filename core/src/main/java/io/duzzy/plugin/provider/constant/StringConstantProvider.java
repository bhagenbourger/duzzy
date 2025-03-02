package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;

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
