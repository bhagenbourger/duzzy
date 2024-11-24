package io.duzzy.cli.documentation;

import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocMarkdownFormatter {

  public static String format(Map<DuzzyType, List<Documentation>> doc) {
    return "# Duzzy components" + "\n"
        + doc
        .keySet()
        .stream()
        .sorted()
        .map(e -> format(e, doc.get(e)))
        .collect(Collectors.joining("\n")) + FOOTER;
  }

  private static String format(DuzzyType duzzyType, List<Documentation> documentations) {
    return "\n## " + duzzyType.getTitle() + "\n"
        + duzzyType.getDescription() + "\n"
        + documentations
        .stream()
        .map(DocMarkdownFormatter::format)
        .collect(Collectors.joining("\n"));
  }

  private static String format(Documentation documentation) {
    return "\n### " + documentation.identifier()
        + "\nIdentifier: " + documentation.identifier()
        + "\nDescription: " + documentation.description()
        + print(
        "\nParameters: \n",
        Arrays
            .stream(documentation.parameters())
            .map(DocMarkdownFormatter::format)
            .collect(Collectors.joining("\n"))
    )
        + print(
        "\nExample: \n```\n",
        documentation.example(),
        "```"
    );
  }

  private static String format(Parameter parameter) {
    return "  - Name: " + parameter.name()
        + print("\n    Aliases: ", String.join(", ", parameter.aliases()))
        + "\n    Description: " + parameter.description()
        + print("\n    Default value: ", parameter.defaultValue());
  }

  private static String print(String header, String value) {
    return print(header, value, "");
  }

  private static String print(String header, String value, String footer) {
    return value.isEmpty() ? "" : header + value + footer;
  }

  private static final String FOOTER = """
      
      
      Qualified name of component \
      must contains "duzzy" \
      to appear into this documentation.""";
}
