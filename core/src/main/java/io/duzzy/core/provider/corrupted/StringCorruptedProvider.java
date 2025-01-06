package io.duzzy.core.provider.corrupted;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;

public interface StringCorruptedProvider extends Provider<String> {

  static String corruptedValue(FieldContext fieldContext, Integer maxLength) {
    final int length = fieldContext.random().nextInt(0, maxLength + 1);
    return fieldContext
        .random()
        .ints(0, 123)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
