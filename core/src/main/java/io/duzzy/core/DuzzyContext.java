package io.duzzy.core;

import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;

import java.util.Random;

public record DuzzyContext(
        SchemaContext schemaContext,
        Sink sink,
        Long rows,
        Long seed
) {
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
        rows = rows == null ? 10L : rows;
        seed = seed == null ? new Random().nextLong() : seed;
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
}
