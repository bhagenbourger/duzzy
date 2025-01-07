package io.duzzy.core.provider;

import io.duzzy.core.Plugin;
import io.duzzy.core.field.FieldContext;

public interface Provider<T> extends Plugin {

  T value(FieldContext fieldContext);

  T corruptedValue(FieldContext fieldContext);
}


