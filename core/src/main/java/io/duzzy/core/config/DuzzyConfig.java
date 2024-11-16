package io.duzzy.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.sink.Sink;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public record DuzzyConfig(
        List<DuzzyConfigColumn> columns,
        Sink sink
) {
    private static final String NAME = "name";
    private static final String COLUMN_TYPE = "columnType";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static DuzzyConfig fromFile(File file) throws IOException {
        return Parser.YAML_MAPPER.readValue(file, DuzzyConfig.class);
    }

    public Optional<Column<?>> findColumn(
            String columnName,
            ColumnType columnType,
            String key,
            String value
    ) {
        //TODO: what if columns if null?
        return columns()
                .stream()
                .filter(c -> c.querySelectorMatcher(key, value))
                .findFirst()
                .map(e -> buildColumn(columnName, columnType, e));
    }

    private static Column<?> buildColumn(
            String columnName,
            ColumnType columnType,
            DuzzyConfigColumn duzzyConfigColumn
    ) {
        final HashMap<String, Object> map = new HashMap<>(duzzyConfigColumn.parameters());
        map.put(NAME, columnName);
        map.put(COLUMN_TYPE, columnType);
        try {
            return OBJECT_MAPPER.convertValue(
                    map,
                    Class.forName(duzzyConfigColumn.identifier()).asSubclass(Column.class)
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
