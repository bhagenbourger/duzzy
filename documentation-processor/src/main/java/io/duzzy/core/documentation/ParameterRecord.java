package io.duzzy.core.documentation;

/**
 * Represents a parameter for a Duzzy element, including its name, possible aliases,
 * description, and default value.
 *
 * @param name         The primary name of the parameter.
 * @param aliases      Alternative names for the parameter.
 * @param description  A description of what the parameter does.
 * @param defaultValue The default value for the parameter, if any.
 */
public record ParameterRecord(
    String name,
    String[] aliases,
    String description,
    String defaultValue) {
}
