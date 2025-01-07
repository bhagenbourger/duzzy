package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public class BooleanRandomProvider implements Provider<Boolean> {

  @JsonCreator
  public BooleanRandomProvider() {
  }

  @Override
  public Boolean value(FieldContext fieldContext) {
    return fieldContext.random().nextBoolean();
  }

  @Override
  public Boolean corruptedValue(FieldContext fieldContext) {
    return value(fieldContext);
  }
}
