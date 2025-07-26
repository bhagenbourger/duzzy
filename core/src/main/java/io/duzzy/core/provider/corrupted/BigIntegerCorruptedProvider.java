package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.math.BigInteger;

public interface BigIntegerCorruptedProvider extends Provider<BigInteger> {

  @Override
  default BigInteger corruptedValue(FieldContext fieldContext) {
    final byte[] bytes = new byte[20];
    fieldContext.random().nextBytes(bytes);
    return new BigInteger(bytes);
  }
}
