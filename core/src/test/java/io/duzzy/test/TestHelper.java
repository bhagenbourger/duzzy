package io.duzzy.test;

import io.duzzy.core.column.ColumnContext;

import java.util.Random;
import java.util.function.Supplier;

public class TestHelper {
    public static Supplier<ColumnContext> DUMMY_COLUMN_CONTEXT =
            () -> new ColumnContext(new Random(1L), 1L, 1L);
}
