package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public interface DoubleCorruptedProvider extends Provider<Double> {
  @Override
  default Double corruptedValue(FieldContext fieldContext) {
    return fieldContext.random().nextDouble(Double.MIN_VALUE, Double.MAX_VALUE);
  }
}
