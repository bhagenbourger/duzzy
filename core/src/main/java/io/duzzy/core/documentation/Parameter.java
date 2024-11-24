package io.duzzy.core.documentation;

public @interface Parameter {

    String name();

    String[] aliases() default {};

    String description();

    String defaultValue() default "";
}
