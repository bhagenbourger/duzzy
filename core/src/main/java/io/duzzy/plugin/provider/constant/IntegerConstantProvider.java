package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.IntegerCorruptedProvider;

public class IntegerConstantProvider extends ConstantProvider<Integer> implements IntegerCorruptedProvider {

    @JsonCreator
    public IntegerConstantProvider(
            @JsonProperty("value") Integer value
    ) {
        super(value);
    }
}
