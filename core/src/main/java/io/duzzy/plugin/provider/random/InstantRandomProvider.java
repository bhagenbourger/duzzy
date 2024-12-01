package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

import java.time.Instant;

public class InstantRandomProvider implements Provider<Instant> {

    private static final Long DEFAULT_MAX = 253402300800000L; //9999-12-31 + 1 day

    private final Long min;
    private final Long max;

    public InstantRandomProvider() {
        this(null, null);
    }

    @JsonCreator
    public InstantRandomProvider(
            @JsonProperty("min") String min,
            @JsonProperty("max") String max
    ) {
        this.min = min == null ? 0 : Instant.parse(min).toEpochMilli();
        this.max = max == null ? DEFAULT_MAX : Instant.parse(max).toEpochMilli();
        assert this.min < this.max : "Min instant must be before max instant";
    }

    @Override
    public Instant value(ColumnContext columnContext) {
        return Instant.ofEpochMilli(columnContext.random().nextLong(this.min, this.max));
    }

    @Override
    public Instant corruptedValue(ColumnContext columnContext) {
        return Instant.ofEpochMilli(columnContext.random().nextLong(Long.MIN_VALUE, Long.MAX_VALUE));
    }
}
