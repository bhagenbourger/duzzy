package io.duzzy.core.provider.constant;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public abstract class ConstantProvider<T> implements Provider<T> {

  private final T value;

  public ConstantProvider(T value) {
    this.value = value;
  }

  @Override
  public T value(FieldContext fieldContext) {
    return value;
  }

  public T getValue() {
    return value;
  }
}
