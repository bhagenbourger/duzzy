package io.duzzy.core.documentation;

/**
 * Represents documentation metadata for a Duzzy element.
 *
 * @param identifier  Unique identifier for the documented element.
 * @param description Description of the element.
 * @param module      The module to which the element belongs.
 * @param duzzyType   The type of the Duzzy element.
 * @param parameters  Parameters associated with the element.
 * @param example     Example usage or value for the element.
 */
public record DocumentationRecord(
    String identifier,
    String description,
    String module,
    DuzzyType duzzyType,
    ParameterRecord[] parameters,
    String example
) {
}
