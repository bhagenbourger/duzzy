package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.time.Instant;

public interface InstantCorruptedProvider extends Provider<Instant> {

  @Override
  default Instant corruptedValue(FieldContext fieldContext) {
    return Instant.ofEpochMilli(fieldContext.random().nextLong(Long.MIN_VALUE, Long.MAX_VALUE));
  }
}
