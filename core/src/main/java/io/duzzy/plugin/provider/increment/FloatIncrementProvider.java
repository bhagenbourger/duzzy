package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class FloatIncrementProvider implements Provider<Float> {

    private final Float start;
    private final Float step;

    @JsonCreator
    public FloatIncrementProvider(
            @JsonProperty("start") Float start,
            @JsonProperty("step") Float step
    ) {
        this.start = start == null ? 0f : start;
        this.step = step == null ? 0.1f : step;
    }

    @Override
    public Float value(ColumnContext columnContext) {
        return start + (columnContext.rowIndex().floatValue() * step);
    }
}
