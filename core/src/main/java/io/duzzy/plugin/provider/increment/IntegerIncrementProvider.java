package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class IntegerIncrementProvider implements Provider<Integer> {

    private final Integer start;
    private final Integer step;

    @JsonCreator
    public IntegerIncrementProvider(
            @JsonProperty("start") Integer start,
            @JsonProperty("step") Integer step
    ) {
        this.start = start == null ? 0 : start;
        this.step = step == null ? 1 : step;
    }

    @Override
    public Integer value(ColumnContext columnContext) {
        return start + (columnContext.rowIndex().intValue() * step);
    }
}
