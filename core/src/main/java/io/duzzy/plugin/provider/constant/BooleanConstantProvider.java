package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.provider.constant.ConstantProvider;

@Documentation(
        identifier = "io.duzzy.plugin.column.constant.BooleanConstantColumn",
        description = "Boolean constant column always return value",
        duzzyType = DuzzyType.PROVIDER,
        parameters = {
                @Parameter(
                        name = "null_rate",
                        aliases = {"nullRate", "null-rate"},
                        description = "Rate of null values, between 0 and 1",
                        defaultValue = "0"
                ),
                @Parameter(
                        name = "value",
                        description = "The constant value, must be a boolean"
                )
        },
        example = """
                ---
                identifier: "io.duzzy.plugin.column.constant.BooleanConstantColumn"
                name: unit
                null_rate: 0
                value: false
                """
)
public class BooleanConstantProvider extends ConstantProvider<Boolean> {

    @JsonCreator
    public BooleanConstantProvider(
            @JsonProperty("value") Boolean value
    ) {
        super(value);
    }

    @Override
    public Boolean corruptedValue(ColumnContext columnContext) {
        return !getValue();
    }
}
