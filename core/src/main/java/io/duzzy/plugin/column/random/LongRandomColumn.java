package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class LongRandomColumn extends Column<Long> {

    private final Long min;
    private final Long max;

    public LongRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null, null, null);
    }

    public LongRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min") Long min,
            @JsonProperty("max") Long max) {
        super(
                name,
                columnType == null ? ColumnType.LONG : columnType,
                nullRate
        );
        this.min = min == null ? Long.MIN_VALUE : min;
        this.max = max == null ? Long.MAX_VALUE : max;
    }

    @Override
    protected Long computeValue(ColumnContext columnContext) {
        return columnContext.random().nextLong(this.min, this.max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongRandomColumn column = (LongRandomColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && Objects.equals(min, column.min)
                && Objects.equals(max, column.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), min, max);
    }
}
