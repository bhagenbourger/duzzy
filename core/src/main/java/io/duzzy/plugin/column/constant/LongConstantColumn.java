package io.duzzy.plugin.column.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.column.ConstantColumn;

import java.util.Objects;

public class LongConstantColumn extends ConstantColumn<Long> {

    @JsonCreator
    public LongConstantColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("value") Long value) {
        super(
                name,
                columnType == null ? ColumnType.LONG : columnType,
                nullRate,
                value
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongConstantColumn column = (LongConstantColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && Objects.equals(super.getValue(), column.getValue());
    }
}
