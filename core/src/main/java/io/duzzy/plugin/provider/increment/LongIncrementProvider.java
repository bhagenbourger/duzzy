package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class LongIncrementProvider implements Provider<Long> {

    private final Long start;
    private final Long step;

    @JsonCreator
    public LongIncrementProvider(
            @JsonProperty("start") Long start,
            @JsonProperty("step") Long step
    ) {
        this.start = start == null ? 0L : start;
        this.step = step == null ? 1L : step;
    }

    @Override
    public Long value(ColumnContext columnContext) {
        return start + (columnContext.rowIndex() * step);
    }
}
