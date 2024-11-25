package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ConstantProvider;

import java.util.Objects;

public class FloatConstantProvider extends ConstantProvider<Float> {

    @JsonCreator
    public FloatConstantProvider(
            @JsonProperty("value") Float value
    ) {
        super(value);
    }
}
