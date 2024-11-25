package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class FloatRandomProvider implements Provider<Float> {

    private final Float min;
    private final Float max;

    public FloatRandomProvider() {
        this(null, null);
    }

    @JsonCreator
    public FloatRandomProvider(
            @JsonProperty("min") Float min,
            @JsonProperty("max") Float max
    ) {
        this.min = min == null ? Float.MIN_VALUE : min;
        this.max = max == null ? Float.MAX_VALUE : max;
    }

    @Override
    public Float value(ColumnContext columnContext) {
        return columnContext.random().nextFloat(this.min, this.max);
    }
}
