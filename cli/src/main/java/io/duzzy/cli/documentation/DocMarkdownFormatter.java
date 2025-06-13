package io.duzzy.cli.documentation;

import io.duzzy.core.documentation.DocumentationRecord;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.ParameterRecord;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocMarkdownFormatter {

  public static String format(Map<DuzzyType, List<DocumentationRecord>> doc) {
    return "# Duzzy components" + "\n"
        + doc
        .keySet()
        .stream()
        .sorted()
        .map(e -> format(e, doc.get(e)))
        .collect(Collectors.joining("\n"));
  }

  private static String format(DuzzyType duzzyType, List<DocumentationRecord> documentations) {
    return "\n## " + duzzyType.getTitle() + "\n"
        + duzzyType.getDescription() + "\n"
        + documentations
        .stream()
        .sorted(Comparator.comparing(DocumentationRecord::identifier))
        .map(DocMarkdownFormatter::format)
        .collect(Collectors.joining("\n"));
  }

  private static String format(DocumentationRecord documentation) {
    return "\n### " + documentation.identifier()
        + "  \nIdentifier: " + documentation.identifier()
        + "  \nDescription: " + documentation.description()
        + "  \nModule: " + documentation.module()
        + print(
        "  \n\nParameters: \n",
        Arrays
            .stream(documentation.parameters())
            .map(DocMarkdownFormatter::format)
            .collect(Collectors.joining("\n"))
    )
        + print(
        "  \n\nExample: \n```\n",
        documentation.example(),
        "```"
    );
  }

  private static String format(ParameterRecord parameter) {
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
}
