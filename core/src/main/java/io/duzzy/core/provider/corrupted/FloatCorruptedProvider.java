package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public interface FloatCorruptedProvider extends Provider<Float> {

  @Override
  default Float corruptedValue(FieldContext fieldContext) {
    return fieldContext.random().nextFloat(Float.MIN_VALUE, Float.MAX_VALUE);
  }
}
