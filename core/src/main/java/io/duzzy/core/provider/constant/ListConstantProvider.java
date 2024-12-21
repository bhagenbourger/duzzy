package io.duzzy.core.provider.constant;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;
import java.util.List;

public abstract class ListConstantProvider<T> implements Provider<T> {

  private final List<T> values;

  public ListConstantProvider(List<T> values) {
    this.values = values;
  }

  @Override
  public T value(ColumnContext columnContext) {
    return values.get(columnContext.random().nextInt(0, values.size()));
  }

  public List<T> getValues() {
    return values;
  }
}
