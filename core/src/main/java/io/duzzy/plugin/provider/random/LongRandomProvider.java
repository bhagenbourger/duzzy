package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class LongRandomProvider implements Provider<Long> {

    private final Long min;
    private final Long max;

    public LongRandomProvider() {
        this(null, null);
    }

    @JsonCreator
    public LongRandomProvider(
            @JsonProperty("min") Long min,
            @JsonProperty("max") Long max
    ) {
        this.min = min == null ? Long.MIN_VALUE : min;
        this.max = max == null ? Long.MAX_VALUE : max;
    }

    @Override
    public Long value(ColumnContext columnContext) {
        return columnContext.random().nextLong(this.min, this.max);
    }
}
