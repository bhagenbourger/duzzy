package io.duzzy.core.column;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.core.provider.Provider;

import java.util.List;
import java.util.Objects;

public record Column(
        @JsonProperty("name") String name,
        @JsonProperty("column_type") @JsonAlias({"columnType", "column-type"}) ColumnType columnType,
        @JsonProperty("null_rate") @JsonAlias({"nullRate", "null-rate"}) Float nullRate,
        @JsonProperty("corrupted_rate") @JsonAlias({"corruptedRate", "corrupted-rate"}) Float corruptedRate,
        @JsonProperty("providers") List<Provider<?>> providers
) {

    private static final Float DEFAULT_RATE = 0f;

    public Column {
        assert name != null && !name.isEmpty() : "Column name can't be null or empty";
        assert columnType != null : "Column type can't be null";
        assert nullRate == null || (nullRate >= 0 && nullRate <= 1) : "Column nullRate must be between 0 and 1";
        assert corruptedRate == null || (corruptedRate >= 0 && corruptedRate <= 1) : "Column corruptedRate must be between 0 and 1";
        assert providers != null && !providers.isEmpty() : "Providers can't be null or empty";
        nullRate = nullRate == null ? DEFAULT_RATE : nullRate;
        corruptedRate = corruptedRate == null ? DEFAULT_RATE : corruptedRate;
    }

    public Object value(ColumnContext columnContext) {
        if ((!Objects.equals(corruptedRate(), DEFAULT_RATE))
                && columnContext.random().nextFloat(0f, 1f) < corruptedRate()) {
            return providers.getFirst().corruptedValue(columnContext);
        }

        if (!Objects.equals(nullRate(), DEFAULT_RATE)
                && columnContext.random().nextFloat(0f, 1f) < nullRate()) {
            return null;
        }

        return providers().getFirst().value(columnContext);
    }
}
