package io.duzzy.core.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.regex.Pattern;

public record Enricher(
    @JsonProperty("query_selector")
    @JsonAlias({"querySelector", "query-selector"})
    String querySelector,

    @JsonProperty("provider_identifier")
    @JsonAlias({"providerIdentifier", "provider-identifier"})
    String providerIdentifier,

    @JsonProperty("provider_parameters")
    @JsonAlias({"providerParameters", "provider-parameters"})
    Map<String, Object> providerParameters
) {

  public boolean querySelectorMatcher(String key, String value) {
    final String[] keyVal = querySelector.split("=");
    return keyVal[0].equals(key) && Pattern.compile(keyVal[1]).matcher(value).matches();
  }
}
