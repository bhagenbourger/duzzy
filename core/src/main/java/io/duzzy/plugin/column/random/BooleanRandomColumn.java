package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.util.Objects;

public class BooleanRandomColumn extends Column<Boolean> {

    public BooleanRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null);
    }

    @JsonCreator
    public BooleanRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate
    ) {
        super(
                name,
                columnType == null ? ColumnType.BOOLEAN : columnType,
                nullRate
        );
    }

    @Override
    protected Boolean computeValue(ColumnContext columnContext) {
        return columnContext.random().nextBoolean();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanRandomColumn column = (BooleanRandomColumn) o;
        return Objects.equals(super.getName(), column.getName())
                && Objects.equals(super.getColumnType(), column.getColumnType())
                && Objects.equals(super.getNullRate(), column.getNullRate());
    }
}
