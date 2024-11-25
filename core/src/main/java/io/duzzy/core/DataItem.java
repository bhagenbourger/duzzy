package io.duzzy.core;

import io.duzzy.core.provider.ColumnType;

public record DataItem(
        String name,
        ColumnType type,
        Object value
) {
}
