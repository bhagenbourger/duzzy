package io.duzzy.core.provider.corrupted;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

public interface IntegerCorruptedProvider extends Provider<Integer> {
  @Override
  default Integer corruptedValue(ColumnContext columnContext) {
    return columnContext.random().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }
}
