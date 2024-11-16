package io.duzzy.plugin.column.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.column.WeightedItem;
import io.duzzy.core.column.WeightedListConstantColumn;

import java.util.List;
import java.util.Objects;

public class StringWeightedListConstantColumn extends WeightedListConstantColumn<String> {

    private static final List<WeightedItem<String>> DEFAULT = List.of(new WeightedItem<>("constant", 1));

    @JsonCreator
    public StringWeightedListConstantColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("values") List<WeightedItem<String>> values) {
        super(
                name,
                columnType == null ? ColumnType.STRING : columnType,
                nullRate,
                values == null || values.isEmpty() ? DEFAULT : values
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringWeightedListConstantColumn column = (StringWeightedListConstantColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && Objects.equals(super.getValues(), column.getValues())
                && Objects.equals(super.getTotalWeight(), column.getTotalWeight());
    }
}
