package io.duzzy.core;

import io.duzzy.core.field.Type;

public record DataItem(
    String name,
    Type type,
    Object value
) {
}
