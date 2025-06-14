package io.duzzy.documentation;

import static java.lang.annotation.RetentionPolicy.CLASS;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS)
@Target(ElementType.TYPE)
public @interface Documentation {
  @JsonProperty("identifier")
  String identifier();

  @JsonProperty("description")
  String description();

  @JsonProperty("module")
  String module();

  @JsonProperty("duzzyType")
  DuzzyType duzzyType();
  
  @JsonProperty("nativeSupport")
  boolean nativeSupport() default false;

  @JsonProperty("parameters")
  Parameter[] parameters() default {};

  @JsonProperty("example")
  String example() default "";
}
