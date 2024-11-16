package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

public class UUIDRandomColumn extends Column<UUID> {

    private static final int LEFT_LIMIT = 48; // numeral '0'
    private static final int RIGHT_LIMIT = 122; // letter 'z'
    private static final int LENGTH = 20;

    @JsonCreator
    public UUIDRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate
    ) {
        super(name, ColumnType.UUID, nullRate);
    }

    @Override
    protected UUID computeValue(ColumnContext columnContext) {
        final String value = columnContext
                .random()
                .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .limit(LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUIDRandomColumn column = (UUIDRandomColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate());
    }
}
