package io.duzzy.core;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;
import java.util.Optional;
import java.util.Random;

public record DuzzyContext(
    DuzzyLimit duzzyLimit,
    DuzzySchema duzzySchema,
    Sink sink,
    Long seed,
    Integer threads
) {
  private static final Random RANDOM = new Random();
  private static final Integer DEFAULT_THREADS = 1;

  public DuzzyContext(DuzzySchema duzzySchema) {
    this(duzzySchema, null);
  }

  public DuzzyContext(DuzzySchema duzzySchema, Sink sink) {
    this(null, duzzySchema, sink, null, null);
  }

  public DuzzyContext {
    duzzyLimit = duzzyLimit == null ? new DuzzyLimit() : duzzyLimit;
    duzzySchema = duzzySchema == null ? new DuzzySchema(Optional.empty(), null) : duzzySchema;
    sink = sink == null ? new ConsoleSink(new JsonSerializer()) : sink;
    seed = seed == null ? Long.valueOf(RANDOM.nextLong()) : seed;
    threads = threads == null ? DEFAULT_THREADS : threads;
  }

  public DuzzyContext withSeed(Long seed) {
    return seed == null ? this : new DuzzyContext(
        this.duzzyLimit,
        this.duzzySchema,
        this.sink,
        seed,
        this.threads
    );
  }

  public DuzzyContext withRows(Long rows) {
    return rows == null ? this : new DuzzyContext(
        this.duzzyLimit.withRows(rows),
        this.duzzySchema,
        this.sink,
        this.seed,
        this.threads);
  }

  public DuzzyContext withSize(Long size) {
    return size == null ? this : new DuzzyContext(
        this.duzzyLimit.withSize(size),
        this.duzzySchema,
        this.sink,
        this.seed,
        this.threads);
  }

  public DuzzyContext withDuration(Long duration) {
    return duration == null ? this : new DuzzyContext(
        this.duzzyLimit.withDuration(duration),
        this.duzzySchema,
        this.sink,
        this.seed,
        this.threads);
  }

  public DuzzyContext withSink(Sink sink) {
    return sink == null ? this : new DuzzyContext(
        this.duzzyLimit,
        this.duzzySchema,
        sink,
        this.seed,
        this.threads);
  }

  public DuzzyContext withThreads(Integer threads) {
    return threads == null ? this : new DuzzyContext(
        this.duzzyLimit,
        this.duzzySchema,
        this.sink,
        this.seed,
        threads);
  }

  @Override
  public Integer threads() {
    return Integer.min(threads, sink.maxThread());
  }
}
