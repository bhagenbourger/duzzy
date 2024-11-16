package io.duzzy.plugin.column.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.ColumnType;

import java.time.LocalDate;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class LocalDateRandomColumn extends Column<LocalDate> {

    private static final Long DEFAULT_MAX = 2932891L; //9999-12-31 + 1 day

    private final Long min;
    private final Long max;

    public LocalDateRandomColumn(
            @JsonProperty("name") String name,
            @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
            @JsonProperty("min") String min,
            @JsonProperty("max") String max) {
        super(name, ColumnType.DATE, nullRate);
        this.min = min == null ? 0 : LocalDate.parse(min, ISO_LOCAL_DATE).toEpochDay();
        this.max = max == null ? DEFAULT_MAX : LocalDate.parse(max, ISO_LOCAL_DATE).toEpochDay();
        assert this.min < this.max : "Min local date must be before max local date in colum " + name;
    }

    @Override
    protected LocalDate computeValue(ColumnContext columnContext) {
        return LocalDate.ofEpochDay(columnContext.random().nextLong(this.min, this.max));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalDateRandomColumn column = (LocalDateRandomColumn) o;
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
