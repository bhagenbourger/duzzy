package io.duzzy.core;

import io.duzzy.core.column.Column;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.column.constant.BooleanConstantColumn;
import io.duzzy.plugin.column.constant.DoubleConstantColumn;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;

import java.util.List;
import java.util.Random;

public record DuzzySchema(
        List<Column<?>> columns,
        Sink sink,
        Long rows,
        Long seed
) {

    private static final List<Column<?>> DEFAULT_COLUMNS = List.of(
            new BooleanConstantColumn("BooleanConstantColumn", null, null, Boolean.TRUE),
            new DoubleConstantColumn("DoubleConstantColumn", null, null, 1.0d)
    );

    public static final DuzzySchema DEFAULT = new DuzzySchema(null, null, null, null);

    public DuzzySchema {
        columns = columns == null || columns.isEmpty() ? DEFAULT_COLUMNS : columns;
        sink = sink == null ? new ConsoleSink(new JsonSerializer()) : sink;
        rows = rows == null ? 10L : rows;
        seed = seed == null ? new Random().nextLong() : seed;
    }

    public DuzzySchema withSeed(Long seed) {
        return seed == null ? this : new DuzzySchema(columns(), sink(), rows(), seed);
    }

    public DuzzySchema withRows(Long rows) {
        return rows == null ? this : new DuzzySchema(columns(), sink(), rows, seed());
    }

    public DuzzySchema withSink(Sink sink) {
        return sink == null ? this : new DuzzySchema(columns(), sink, rows(), seed());
    }
}
