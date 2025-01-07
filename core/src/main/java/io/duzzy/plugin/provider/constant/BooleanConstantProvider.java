package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.ConstantProvider;

public class BooleanConstantProvider extends ConstantProvider<Boolean> {

  @JsonCreator
  public BooleanConstantProvider(
      @JsonProperty("value") Boolean value
  ) {
    super(value);
  }

  @Override
  public Boolean corruptedValue(FieldContext fieldContext) {
    return !getValue();
  }
}
