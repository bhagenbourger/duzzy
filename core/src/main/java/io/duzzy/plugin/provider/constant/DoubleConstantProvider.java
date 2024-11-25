package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ConstantProvider;

public class DoubleConstantProvider extends ConstantProvider<Double> {

    @JsonCreator
    public DoubleConstantProvider(
            @JsonProperty("value") Double value
    ) {
        super(value);
    }
}
