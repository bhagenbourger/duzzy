package io.duzzy.test;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.plugin.provider.random.LongRandomProvider;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class TestUtility {
  public static final Supplier<ColumnContext> SEEDED_ONE_COLUMN_CONTEXT =
      () -> new ColumnContext(List.of(), false, new Random(1L), 1L, 1L);

  public static final Supplier<ColumnContext> SEEDED_FIVE_COLUMN_CONTEXT =
      () -> new ColumnContext(List.of(), false, new Random(5L), 5L, 5L);

  public static final Supplier<ColumnContext> RANDOM_COLUMN_CONTEXT =
      () -> new ColumnContext(List.of(), false, new Random(), 0L, 0L);

  public static Supplier<ColumnContext> providersColumnContext(Boolean hasSchema) {
    return () -> new ColumnContext(
        List.of(new LongRandomProvider()),
        hasSchema,
        new Random(),
        0L,
        0L
    );
  }
}
