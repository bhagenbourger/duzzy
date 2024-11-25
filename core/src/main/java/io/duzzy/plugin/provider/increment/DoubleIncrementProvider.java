package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class DoubleIncrementProvider implements Provider<Double> {

    private final Double start;
    private final Double step;

    @JsonCreator
    public DoubleIncrementProvider(
            @JsonProperty("start") Double start,
            @JsonProperty("step") Double step
    ) {
        this.start = start == null ? 0d : start;
        this.step = step == null ? 0.1d : step;
    }

    @Override
    public Double value(ColumnContext columnContext) {
        return start + (columnContext.rowIndex().doubleValue() * step);
    }
}
