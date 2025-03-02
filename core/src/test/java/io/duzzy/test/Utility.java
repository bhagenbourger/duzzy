package io.duzzy.test;

import io.duzzy.core.field.FieldContext;
import io.duzzy.plugin.provider.random.LongRandomProvider;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Utility {
  public static final Supplier<FieldContext> SEEDED_ONE_FIELD_CONTEXT =
      () -> new FieldContext(List.of(), false, new Random(1L), 1L, 1L);

  public static final Supplier<FieldContext> SEEDED_FIVE_FIELD_CONTEXT =
      () -> new FieldContext(List.of(), false, new Random(5L), 5L, 5L);

  public static final Supplier<FieldContext> RANDOM_FIELD_CONTEXT =
      () -> new FieldContext(List.of(), false, new Random(), 0L, 0L);

  public static Supplier<FieldContext> providersFieldContext(Boolean hasSchema) {
    return () -> new FieldContext(
        List.of(new LongRandomProvider()),
        hasSchema,
        new Random(),
        0L,
        0L
    );
  }
}
