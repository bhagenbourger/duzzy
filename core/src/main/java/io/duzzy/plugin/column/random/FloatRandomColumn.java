package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class FloatRandomColumn extends Column<Float> {

    private final Float min;
    private final Float max;

    public FloatRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null, null, null);
    }

    public FloatRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min") Float min,
            @JsonProperty("max") Float max) {
        super(
                name,
                columnType == null ? ColumnType.FLOAT : columnType,
                nullRate
        );
        this.min = min == null ? Float.MIN_VALUE : min;
        this.max = max == null ? Float.MAX_VALUE : max;
    }

    @Override
    protected Float computeValue(ColumnContext columnContext) {
        return columnContext.random().nextFloat(this.min, this.max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatRandomColumn column = (FloatRandomColumn) o;
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
