package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.time.Instant;
import java.util.Objects;

public class InstantRandomColumn extends Column<Instant> {

    private static final Long DEFAULT_MAX = 253402300800000L; //9999-12-31 + 1 day

    private final Long min;
    private final Long max;

    public InstantRandomColumn(String name, ColumnType columnType) {
        this(name, columnType, null, null, null);
    }

    public InstantRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min") String min,
            @JsonProperty("max") String max) {
        super(
                name,
                columnType == null ? ColumnType.TIMESTAMP_MILLIS : columnType,
                nullRate
        );
        this.min = min == null ? 0 : Instant.parse(min).toEpochMilli();
        this.max = max == null ? DEFAULT_MAX : Instant.parse(max).toEpochMilli();
        assert this.min < this.max : "Min instant must be before max instant in colum " + name;
    }

    @Override
    protected Instant computeValue(ColumnContext columnContext) {
        return Instant.ofEpochMilli(columnContext.random().nextLong(this.min, this.max));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstantRandomColumn column = (InstantRandomColumn) o;
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
