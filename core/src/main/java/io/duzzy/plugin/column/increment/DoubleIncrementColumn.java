package io.duzzy.plugin.column.increment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class DoubleIncrementColumn extends Column<Double> {

    private final Double start;
    private final Double step;

    @JsonCreator
    public DoubleIncrementColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("start") Double start,
            @JsonProperty("step") Double step) {
        super(
                name,
                columnType == null ? ColumnType.DOUBLE : columnType,
                nullRate
        );
        this.start = start == null ? 0d : start;
        this.step = step == null ? 0.1d : step;
    }

    @Override
    protected Double computeValue(ColumnContext columnContext) {
        return start + (columnContext.rowIndex().doubleValue() * step);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleIncrementColumn column = (DoubleIncrementColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && Objects.equals(start, column.start)
                && Objects.equals(step, column.step);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, step);
    }
}
