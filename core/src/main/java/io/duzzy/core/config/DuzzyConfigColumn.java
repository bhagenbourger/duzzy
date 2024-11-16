package io.duzzy.core.config;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Map;
import java.util.regex.Pattern;

public record DuzzyConfigColumn(
        @JsonAlias({"query_selector", "querySelector", "query-selector"}) String querySelector,
        String identifier,
        Map<String, Object> parameters
) {

    public boolean querySelectorMatcher(String key, String value) {
        final String[] keyVal = querySelector.split("=");
        return keyVal[0].equals(key) && Pattern.compile(keyVal[1]).matcher(value).matches();
    }
}
