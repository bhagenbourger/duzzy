package io.duzzy.core.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;

import java.util.Map;
import java.util.regex.Pattern;

@Documentation(
        identifier = "io.duzzy.core.config.DuzzyConfigColumn",
        description = "",
        duzzyType = DuzzyType.DUZZY_CONFIG_COLUMN,
        parameters = {},
        example = ""
)
public record DuzzyConfigColumn(
        @JsonProperty("query_selector") @JsonAlias({"querySelector", "query-selector"}) String querySelector,
        @JsonProperty("identifier") String identifier,
        @JsonProperty("parameters") Map<String, Object> parameters
) {

    public boolean querySelectorMatcher(String key, String value) {
        final String[] keyVal = querySelector.split("=");
        return keyVal[0].equals(key) && Pattern.compile(keyVal[1]).matcher(value).matches();
    }
}
