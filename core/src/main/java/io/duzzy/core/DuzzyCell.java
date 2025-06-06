package io.duzzy.core;

import io.duzzy.core.field.Type;

public record DuzzyCell(
    String name,
    Type type,
    Object value
) {
}
