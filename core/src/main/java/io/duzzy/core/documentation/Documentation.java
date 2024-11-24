package io.duzzy.core.documentation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Documentation {
    String identifier();

    String description();

    DuzzyType duzzyType();

    Parameter[] parameters() default {};

    String example();
}
