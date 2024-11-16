package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class DoubleRandomColumn extends Column<Double> {

    private final Double min;
    private final Double max;

    public DoubleRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null, null, null);
    }

    @JsonCreator
    public DoubleRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min") Double min,
            @JsonProperty("max") Double max) {
        super(
                name,
                columnType == null ? ColumnType.DOUBLE : columnType,
                nullRate
        );
        this.min = min == null ? Double.MIN_VALUE : min;
        this.max = max == null ? Double.MAX_VALUE : max;
    }

    @Override
    protected Double computeValue(ColumnContext columnContext) {
        return columnContext.random().nextDouble(this.min, this.max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleRandomColumn column = (DoubleRandomColumn) o;
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
