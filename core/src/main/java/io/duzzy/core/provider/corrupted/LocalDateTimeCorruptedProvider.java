package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface LocalDateTimeCorruptedProvider extends Provider<LocalDateTime> {

  @Override
  default LocalDateTime corruptedValue(FieldContext fieldContext) {
    final long minEpoch = LocalDateTime.MIN.toEpochSecond(ZoneOffset.UTC);
    final long maxEpoch = LocalDateTime.MAX.toEpochSecond(ZoneOffset.UTC);
    final long randomEpoch = fieldContext.random().nextLong(minEpoch, maxEpoch);
    final int randomNano = fieldContext.random().nextInt(0, 1_000_000_000);
    return LocalDateTime.ofEpochSecond(randomEpoch, randomNano, ZoneOffset.UTC);
  }
}
