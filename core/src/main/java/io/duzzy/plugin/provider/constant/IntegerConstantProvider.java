package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.core.provider.ConstantProvider;

import java.util.Objects;

public class IntegerConstantProvider extends ConstantProvider<Integer> {

    @JsonCreator
    public IntegerConstantProvider(
            @JsonProperty("value") Integer value
    ) {
        super(value);
    }
}
