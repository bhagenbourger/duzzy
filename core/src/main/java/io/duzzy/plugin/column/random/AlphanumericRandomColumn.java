package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class AlphanumericRandomColumn extends Column<String> {

    private static final int LEFT_LIMIT = 48; // numeral '0'
    private static final int RIGHT_LIMIT = 122; // letter 'z'

    private final int minLength;
    private final int maxLength;

    public AlphanumericRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null, null, null);
    }

    @JsonCreator
    public AlphanumericRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min_length") @JsonAlias({"minLength", "min-length"}) Integer minLength,
            @JsonProperty("max_length") @JsonAlias({"maxLength", "max-length"}) Integer maxLength) {
        super(
                name,
                columnType == null ? ColumnType.STRING : columnType,
                nullRate
        );
        this.minLength = minLength == null ? 10 : minLength;
        this.maxLength = maxLength == null ? 15 : maxLength;
    }

    @Override
    protected String computeValue(ColumnContext columnContext) {
        int length = columnContext.random().nextInt(minLength, maxLength + 1);
        return columnContext
                .random()
                .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlphanumericRandomColumn column = (AlphanumericRandomColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && minLength == column.minLength
                && maxLength == column.maxLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minLength, maxLength);
    }
}
