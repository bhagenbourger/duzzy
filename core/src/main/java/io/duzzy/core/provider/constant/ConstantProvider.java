package io.duzzy.core.provider.constant;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public abstract class ConstantProvider<T> implements Provider<T> {

  private final T constantValue;

  public ConstantProvider(T constantValue) {
    this.constantValue = constantValue;
  }

  @Override
  public T value(FieldContext fieldContext) {
    return constantValue;
  }

  public T getConstantValue() {
    return constantValue;
  }
}
