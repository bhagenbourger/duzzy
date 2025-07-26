package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.math.BigDecimal;
import java.math.BigInteger;

public interface BigDecimalCorruptedProvider extends Provider<BigDecimal> {

  @Override
  default BigDecimal corruptedValue(FieldContext fieldContext) {
    final BigInteger unscaledVal = BigInteger.valueOf(fieldContext.random().nextLong());
    final int scale = fieldContext.random().nextInt();
    return new BigDecimal(unscaledVal, scale);
  }
}
