package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.WeightedItem;
import io.duzzy.core.provider.WeightedListConstantProvider;

import java.util.List;

public class StringWeightedListConstantProvider extends WeightedListConstantProvider<String> {

    private static final List<WeightedItem<String>> DEFAULT = List.of(new WeightedItem<>("constant", 1));

    @JsonCreator
    public StringWeightedListConstantProvider(
            @JsonProperty("values") List<WeightedItem<String>> values
    ) {
        super(values == null || values.isEmpty() ? DEFAULT : values);
    }
}
