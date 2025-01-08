package io.duzzy.core.documentation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Documentation {
  String identifier();

  String description();

  DuzzyType duzzyType();

  Parameter[] parameters() default {};

  String example();
}
