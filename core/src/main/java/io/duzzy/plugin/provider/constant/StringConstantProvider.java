package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ConstantProvider;

import java.util.Objects;

public class StringConstantProvider extends ConstantProvider<String> {

    private static final String DEFAULT_VALUE = "constant";

    @JsonCreator
    public StringConstantProvider(
            @JsonProperty("value") String value
    ) {
        super(value == null ? DEFAULT_VALUE : value);
    }
}
