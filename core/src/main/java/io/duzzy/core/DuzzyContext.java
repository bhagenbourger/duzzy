package io.duzzy.core;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;
import java.util.Random;

public record DuzzyContext(
    DuzzySchema duzzySchema,
    Sink sink,
    Long rows,
    Long seed,
    Integer threads
) {
  private static final Random RANDOM = new Random();
  private static final Long DEFAULT_ROWS = 10L;
  private static final Integer DEFAULT_THREADS = 1;

  public DuzzyContext(DuzzySchema duzzySchema) {
    this(duzzySchema, null);
  }

  public DuzzyContext(DuzzySchema duzzySchema, Sink sink) {
    this(duzzySchema, sink, null, null, null);
  }

  public DuzzyContext {
    duzzySchema = duzzySchema == null ? new DuzzySchema(null) : duzzySchema;
    sink = sink == null ? new ConsoleSink(new JsonSerializer()) : sink;
    rows = rows == null ? DEFAULT_ROWS : rows;
    seed = seed == null ? Long.valueOf(RANDOM.nextLong()) : seed;
    threads = threads == null ? DEFAULT_THREADS : threads;
  }

  public DuzzyContext withSeed(Long seed) {
    return seed == null ? this : new DuzzyContext(
        this.duzzySchema,
        this.sink,
        this.rows,
        seed,
        this.threads
    );
  }

  public DuzzyContext withRows(Long rows) {
    return rows == null ? this : new DuzzyContext(
        this.duzzySchema,
        this.sink,
        rows,
        this.seed,
        this.threads);
  }

  public DuzzyContext withSink(Sink sink) {
    return sink == null ? this : new DuzzyContext(
        this.duzzySchema,
        sink,
        this.rows,
        this.seed,
        this.threads);
  }

  public DuzzyContext withThreads(Integer threads) {
    return threads == null ? this : new DuzzyContext(
        this.duzzySchema,
        this.sink,
        this.rows,
        this.seed,
        threads);
  }

  @Override
  public Integer threads() {
    return Integer.min(threads, sink.maxThread());
  }
}
