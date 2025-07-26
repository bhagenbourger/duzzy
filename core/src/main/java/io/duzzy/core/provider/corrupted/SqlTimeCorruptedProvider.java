package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.sql.Time;

public interface SqlTimeCorruptedProvider extends Provider<Time> {

  @Override
  default Time corruptedValue(FieldContext fieldContext) {
    return new Time(fieldContext.random().nextLong());
  }
}
