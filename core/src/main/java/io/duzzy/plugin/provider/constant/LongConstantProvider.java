package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ConstantProvider;

import java.util.Objects;

public class LongConstantProvider extends ConstantProvider<Long> {

    @JsonCreator
    public LongConstantProvider(
            @JsonProperty("value") Long value
    ) {
        super(value);
    }
}
