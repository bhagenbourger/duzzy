package io.duzzy.core;

import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

public record DuzzyContext(
    SchemaContext schemaContext,
    Sink sink,
    Long rows,
    Long seed
) {
  private static final Random RANDOM = new Random();
  private static final Long DEFAULT_ROWS = 10L;

  public static final DuzzyContext DEFAULT = new DuzzyContext(
      null,
      null,
      null,
      null
  );

  public DuzzyContext(SchemaContext schemaContext) {
    this(schemaContext, null);
  }

  public DuzzyContext(SchemaContext schemaContext, Sink sink) {
    this(schemaContext, sink, null, null);
  }

  public DuzzyContext {
    schemaContext = schemaContext == null ? new SchemaContext(null, null) : schemaContext;
    sink = sink == null ? new ConsoleSink(new JsonSerializer()) : sink;
    rows = rows == null ? DEFAULT_ROWS : rows;
    seed = seed == null ? Long.valueOf(RANDOM.nextLong()) : seed;
  }

  public DuzzyContext withSeed(Long seed) {
    return seed == null ? this : new DuzzyContext(schemaContext(), sink(), rows(), seed);
  }

  public DuzzyContext withRows(Long rows) {
    return rows == null ? this : new DuzzyContext(schemaContext(), sink(), rows, seed());
  }

  public DuzzyContext withSink(Sink sink) {
    return sink == null ? this : new DuzzyContext(schemaContext(), sink, rows(), seed());
  }

  public Optional<List<String>> checkArguments() {
    final List<String> errors = new ArrayList<>();
    if (rows <= 0) {
      errors.add("Rows parameter must be greater than 0");
    }
    final List<String> allErrors =
        Stream.concat(
                Stream
                    .concat(
                        Optional.of(errors).stream(),
                        sink.checkArguments().stream()
                    ),
                schemaContext.checkArguments().stream()
            )
            .flatMap(List::stream)
            .toList();
    return Optional.of(allErrors).filter(e -> !e.isEmpty());
  }
}
