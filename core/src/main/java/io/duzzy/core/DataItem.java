package io.duzzy.core;

import io.duzzy.core.column.ColumnType;

public record DataItem(
        String name,
        ColumnType type,
        Object value
) {
}
