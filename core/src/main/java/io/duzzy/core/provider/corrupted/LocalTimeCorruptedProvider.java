package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.time.LocalTime;

public interface LocalTimeCorruptedProvider extends Provider<LocalTime> {

  @Override
  default LocalTime corruptedValue(FieldContext fieldContext) {
    return LocalTime.ofNanoOfDay(
        fieldContext.random().nextLong(0, LocalTime.MAX.toNanoOfDay() + 1));
  }
}
