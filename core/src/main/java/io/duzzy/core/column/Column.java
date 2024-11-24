package io.duzzy.core.column;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.provider.Provider;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Documentation(
        identifier = "io.duzzy.core.column.Column",
        description = "A column representation with a name and a type which manages how the data is generated. " +
                "Column delegates data generation to the provider.",
        duzzyType = DuzzyType.COLUMN,
        parameters = {
                @Parameter(
                        name = "name",
                        description = "The column name, must be a string"
                ),
                @Parameter(
                        name = "column_type",
                        aliases = {"columnType", "column-rate"},
                        description = "The column value type, must be in //TODO"
                ),
                @Parameter(
                        name = "null_rate",
                        aliases = {"nullRate", "null-rate"},
                        description = "Rate of null values, between 0.0 and 1.0",
                        defaultValue = "0.0"
                ),
                @Parameter(
                        name = "corrupted_rate",
                        aliases = {"corruptedRate", "corrupted-rate"},
                        description = "Rate of corrupted values, between 0.0 and 1.0",
                        defaultValue = "0.0"
                ),
                @Parameter(
                        name = "providers",
                        description = "The providers list used to generate the column value"
                )
        },
        example = """
                ---
                identifier: "io.duzzy.plugin.column.constant.BooleanConstantColumn"
                null_rate: 0.0
                value: false
                """
)
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
            if (columnContext.hasSchema()) {
                return getProvider(providers(), columnContext.random()).corruptedValue(columnContext);
            }
            return getProvider(columnContext.providers(), columnContext.random()).value(columnContext);
        }

        if (!Objects.equals(nullRate(), DEFAULT_RATE)
                && columnContext.random().nextFloat(0f, 1f) < nullRate()) {
            return null;
        }

        return getProvider(providers(), columnContext.random()).value(columnContext);
    }

    private static Provider<?> getProvider(
            List<Provider<?>> providers,
            Random random
    ) {
        return providers.get(random.nextInt(0, providers.size()));
    }
}
