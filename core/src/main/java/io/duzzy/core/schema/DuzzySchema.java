package io.duzzy.core.schema;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import io.duzzy.plugin.provider.constant.DoubleConstantProvider;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;

import java.util.List;
import java.util.Random;

public record DuzzySchema(
        @JsonProperty("input_schema") @JsonAlias({"inputSchema", "input-schema"}) InputSchema inputSchema,
        List<Column> columns,
        Sink sink,
        Long rows,
        Long seed
) {

    private static final List<Column> DEFAULT_COLUMNS = List.of(
            new Column(
                    "BooleanConstantColumn",
                    ColumnType.BOOLEAN,
                    0f,
                    List.of(new BooleanConstantProvider(Boolean.TRUE))
            ),
            new Column(
                    "DoubleConstantColumn",
                    ColumnType.DOUBLE,
                    0f,
                    List.of(new DoubleConstantProvider(1.0d))
            )
    );

    public static final DuzzySchema DEFAULT = new DuzzySchema(null, null, null, null, null);

    public DuzzySchema {
        columns = columns == null || columns.isEmpty() ? DEFAULT_COLUMNS : columns;
        sink = sink == null ? new ConsoleSink(new JsonSerializer()) : sink;
        rows = rows == null ? 10L : rows;
        seed = seed == null ? new Random().nextLong() : seed;
    }

    public DuzzySchema withSeed(Long seed) {
        return seed == null ? this : new DuzzySchema(inputSchema(), columns(), sink(), rows(), seed);
    }

    public DuzzySchema withRows(Long rows) {
        return rows == null ? this : new DuzzySchema(inputSchema(), columns(), sink(), rows, seed());
    }

    public DuzzySchema withSink(Sink sink) {
        return sink == null ? this : new DuzzySchema(inputSchema(), columns(), sink, rows(), seed());
    }
}
