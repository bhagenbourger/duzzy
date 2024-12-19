package io.duzzy.core.provider.corrupted;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

public interface DoubleCorruptedProvider extends Provider<Double> {
  @Override
  default Double corruptedValue(ColumnContext columnContext) {
    return columnContext.random().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);
  }
}
