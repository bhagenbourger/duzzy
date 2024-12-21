package io.duzzy.core.provider.corrupted;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

public interface FloatCorruptedProvider extends Provider<Float> {

  @Override
  default Float corruptedValue(ColumnContext columnContext) {
    return columnContext.random().nextFloat(Float.MIN_VALUE, Float.MAX_VALUE);
  }
}
