package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class LocalDateRandomProvider implements Provider<LocalDate> {

    private static final Long DEFAULT_MAX = 2932891L; //9999-12-31 + 1 day

    private final Long min;
    private final Long max;

    public LocalDateRandomProvider() {
        this(null, null);
    }

    @JsonCreator
    public LocalDateRandomProvider(
            @JsonProperty("min") String min,
            @JsonProperty("max") String max
    ) {
        this.min = min == null ? 0 : LocalDate.parse(min, ISO_LOCAL_DATE).toEpochDay();
        this.max = max == null ? DEFAULT_MAX : LocalDate.parse(max, ISO_LOCAL_DATE).toEpochDay();
        assert this.min < this.max : "Min local date must be before max local date";
    }

    @Override
    public LocalDate value(ColumnContext columnContext) {
        return LocalDate.ofEpochDay(columnContext.random().nextLong(this.min, this.max));
    }
}
