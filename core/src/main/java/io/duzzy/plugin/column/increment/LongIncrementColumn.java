package io.duzzy.plugin.column.increment;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class LongIncrementColumn extends Column<Long> {

    private final Long start;
    private final Long step;

    @JsonCreator
    public LongIncrementColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("start") Long start,
            @JsonProperty("step") Long step) {
        super(
                name,
                columnType == null ? ColumnType.LONG : columnType,
                nullRate
        );
        this.start = start == null ? 0L : start;
        this.step = step == null ? 1L : step;
    }

    @Override
    protected Long computeValue(ColumnContext columnContext) {
        return start + (columnContext.rowIndex() * step);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongIncrementColumn column = (LongIncrementColumn) o;
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
