package io.duzzy.core.provider.constant;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.util.List;

public abstract class ListConstantProvider<T> implements Provider<T> {

  private final List<T> values;

  public ListConstantProvider(List<T> values) {
    this.values = values;
  }

  @Override
  public T value(FieldContext fieldContext) {
    return values.get(Math.abs(fieldContext.rowId().intValue() % values.size()));
  }

  public List<T> getValues() {
    return values;
  }
}
