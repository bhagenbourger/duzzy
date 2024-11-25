package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;

public class BooleanRandomProvider implements Provider<Boolean> {

    @JsonCreator
    public BooleanRandomProvider() {
    }

    @Override
    public Boolean value(ColumnContext columnContext) {
        return columnContext.random().nextBoolean();
    }
}
