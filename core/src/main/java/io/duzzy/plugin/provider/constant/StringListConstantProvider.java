package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.ListConstantProvider;

import java.util.List;
import java.util.Objects;

public class StringListConstantProvider extends ListConstantProvider<String> {

    @JsonCreator
    public StringListConstantProvider(
            @JsonProperty("values") List<String> values
    ) {
        super(values == null || values.isEmpty() ? List.of("constant") : values);
    }
}
