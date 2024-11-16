package io.duzzy.plugin.column.increment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class IntegerIncrementColumn extends Column<Integer> {

    private final Integer start;
    private final Integer step;

    @JsonCreator
    public IntegerIncrementColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("start") Integer start,
            @JsonProperty("step") Integer step) {
        super(
                name,
                columnType == null ? ColumnType.INTEGER : columnType,
                nullRate
        );
        this.start = start == null ? 0 : start;
        this.step = step == null ? 1 : step;
    }

    @Override
    protected Integer computeValue(ColumnContext columnContext) {
        return start + (columnContext.rowIndex().intValue() * step);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerIncrementColumn column = (IntegerIncrementColumn) o;
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
