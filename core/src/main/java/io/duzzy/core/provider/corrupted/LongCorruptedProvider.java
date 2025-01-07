package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public interface LongCorruptedProvider extends Provider<Long> {

  @Override
  default Long corruptedValue(FieldContext fieldContext) {
    return fieldContext.random().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
  }
}
