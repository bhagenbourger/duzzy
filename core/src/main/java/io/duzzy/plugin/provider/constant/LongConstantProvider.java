package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.LongCorruptedProvider;

public class LongConstantProvider extends ConstantProvider<Long> implements LongCorruptedProvider {

    @JsonCreator
    public LongConstantProvider(
            @JsonProperty("value") Long value
    ) {
        super(value);
    }
}
