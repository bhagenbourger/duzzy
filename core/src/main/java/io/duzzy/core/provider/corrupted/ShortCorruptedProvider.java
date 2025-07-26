package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public interface ShortCorruptedProvider extends Provider<Short> {

  @Override
  default Short corruptedValue(FieldContext fieldContext) {
    return (short) fieldContext.random().nextInt(Short.MIN_VALUE, Short.MAX_VALUE);
  }
}
