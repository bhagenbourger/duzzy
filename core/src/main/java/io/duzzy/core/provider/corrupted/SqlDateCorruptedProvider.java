package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.sql.Date;

public interface SqlDateCorruptedProvider extends Provider<Date> {

  @Override
  default Date corruptedValue(FieldContext fieldContext) {
    return new Date(fieldContext.random().nextLong());
  }
}
