package io.duzzy.core.documentation;

import com.fasterxml.jackson.annotation.JsonProperty;

public @interface Parameter {

  @JsonProperty("name")
  String name();

  @JsonProperty("aliases")
  String[] aliases() default {};

  @JsonProperty("description")
  String description();

  @JsonProperty("defaultValue")
  String defaultValue() default "";
}
