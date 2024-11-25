package io.duzzy.core.schema;

import io.duzzy.core.column.Column;
import io.duzzy.core.sink.Sink;

import java.util.List;

public record DuzzySchema(
        List<Column> columns,
        Sink sink,
        Long rows,
        Long seed
) {
}
