package io.duzzy.plugin.column.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.column.ListConstantColumn;

import java.util.List;
import java.util.Objects;

public class StringListConstantColumn extends ListConstantColumn<String> {

    @JsonCreator
    public StringListConstantColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("values") List<String> values) {
        super(
                name,
                columnType == null ? ColumnType.STRING : columnType,
                nullRate,
                values == null || values.isEmpty() ? List.of("constant") : values
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringListConstantColumn column = (StringListConstantColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && Objects.equals(super.getValues(), column.getValues());
    }
}
