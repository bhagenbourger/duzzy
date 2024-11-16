package io.duzzy.core.column;

import java.util.Random;

public record ColumnContext(
        Random random,
        Long rowId,
        Long rowIndex
) {
    public static final ColumnContext DEFAULT = new ColumnContext(new Random(), 0L, 0L);
}
