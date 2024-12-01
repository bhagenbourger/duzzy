package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.constant.ConstantProvider;

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
