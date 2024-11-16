package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class IntegerRandomColumn extends Column<Integer> {

    private final Integer min;
    private final Integer max;

    public IntegerRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null, null, null);
    }

    public IntegerRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min") Integer min,
            @JsonProperty("max") Integer max) {
        super(
                name,
                columnType == null ? ColumnType.INTEGER : columnType,
                nullRate
        );
        this.min = min == null ? Integer.MIN_VALUE : min;
        this.max = max == null ? Integer.MAX_VALUE : max;
    }

    @Override
    protected Integer computeValue(ColumnContext columnContext) {
        return columnContext.random().nextInt(this.min, this.max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerRandomColumn column = (IntegerRandomColumn) o;
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
