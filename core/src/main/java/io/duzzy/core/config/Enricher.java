package io.duzzy.core.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  public Optional<List<String>> checkArguments() {
    final List<String> errors = new ArrayList<>();
    if (querySelector == null) {
      errors.add("Query selector is null in Enricher");
    } else if (querySelector.split("=").length != 2) {
      errors.add(
          "Query selector " + querySelector + " is invalid in Enricher. "
              + "Query selector must be formatted like that: key=value"
      );
    }
    if (providerIdentifier == null) {
      errors.add("Provider identifier is null in Enricher");
    }
    return Optional.of(errors).filter(e -> !e.isEmpty());
  }
}
