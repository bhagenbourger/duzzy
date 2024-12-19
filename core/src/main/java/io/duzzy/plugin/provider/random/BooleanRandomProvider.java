package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

public class BooleanRandomProvider implements Provider<Boolean> {

  @JsonCreator
  public BooleanRandomProvider() {
  }

  @Override
  public Boolean value(ColumnContext columnContext) {
    return columnContext.random().nextBoolean();
  }

  @Override
  public Boolean corruptedValue(ColumnContext columnContext) {
    return value(columnContext);
  }
}
