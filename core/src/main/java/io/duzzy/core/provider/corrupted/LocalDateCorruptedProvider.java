package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.time.LocalDate;

public interface LocalDateCorruptedProvider extends Provider<LocalDate> {

  @Override
  default LocalDate corruptedValue(FieldContext fieldContext) {
    return LocalDate.ofEpochDay(fieldContext.random().nextLong(LocalDate.MIN.toEpochDay(),
        LocalDate.MAX.toEpochDay()));
  }
}
