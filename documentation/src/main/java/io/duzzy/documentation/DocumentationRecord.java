package io.duzzy.documentation;

/**
 * Represents documentation metadata for a Duzzy element.
 *
 * @param identifier    Unique identifier for the documented element.
 * @param description   Description of the element.
 * @param module        The module to which the element belongs.
 * @param duzzyType     The type of the Duzzy element.
 * @param nativeSupport Indicates if the element has native support in Duzzy.
 * @param parameters    Parameters associated with the element.
 * @param example       Example usage or value for the element.
 */
public record DocumentationRecord(
    String identifier,
    String description,
    String module,
    DuzzyType duzzyType,
    boolean nativeSupport,
    ParameterRecord[] parameters,
    String example
) {

  private static final String UNKNOWN = "Unknown";

  public DocumentationRecord {
    identifier = identifier == null || identifier.isBlank() ? UNKNOWN : identifier;
    description = description == null || description.isBlank() ? UNKNOWN : description;
    module = module == null || module.isBlank() ? UNKNOWN : module;
    duzzyType = duzzyType == null ? DuzzyType.UNKNOWN : duzzyType;
  }
}
