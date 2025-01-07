package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public interface IntegerCorruptedProvider extends Provider<Integer> {
  @Override
  default Integer corruptedValue(FieldContext fieldContext) {
    return fieldContext.random().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }
}
