package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ConstantProvider;

import java.util.Objects;

public class BooleanConstantProvider extends ConstantProvider<Boolean> {

    @JsonCreator
    public BooleanConstantProvider(
            @JsonProperty("value") Boolean value
    ) {
        super(value);
    }
}
