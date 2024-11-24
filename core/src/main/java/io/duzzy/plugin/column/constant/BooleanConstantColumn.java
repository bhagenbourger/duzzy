package io.duzzy.plugin.column.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.column.ConstantColumn;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;

import java.util.Objects;

@Documentation(
        identifier = "io.duzzy.plugin.column.constant.BooleanConstantColumn",
        description = "Boolean constant column always return value",
        duzzyType = DuzzyType.COLUMN,
        parameters = {
                @Parameter(
                        name = "null_rate",
                        aliases = {"nullRate", "null-rate"},
                        description = "Rate of null values, between 0 and 1",
                        defaultValue = "0"
                ),
                @Parameter(
                        name = "value",
                        description = "The constant value, must be a boolean"
                )
        },
        example = """
                ---
                identifier: "io.duzzy.plugin.column.constant.BooleanConstantColumn"
                name: unit
                null_rate: 0
                value: false
                """
)
public class BooleanConstantColumn extends ConstantColumn<Boolean> {

    @JsonCreator
    public BooleanConstantColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("value") Boolean value) {
        super(
                name,
                columnType == null ? ColumnType.BOOLEAN : columnType,
                nullRate,
                value
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanConstantColumn column = (BooleanConstantColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate())
                && Objects.equals(super.getValue(), column.getValue());
    }
}
