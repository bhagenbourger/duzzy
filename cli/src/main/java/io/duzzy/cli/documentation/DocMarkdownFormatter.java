package io.duzzy.cli.documentation;

import io.duzzy.documentation.DocumentationRecord;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.ParameterRecord;
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
    final String lineSeparator = "  \n";
    return "\n### " + documentation.identifier() + icons(documentation.nativeSupport())
        + lineSeparator + "🔑" + " Identifier: " + documentation.identifier()
        + lineSeparator + "📋" + " Description: " + documentation.description()
        + lineSeparator + "📦" + " Module: " + documentation.module()
        + lineSeparator + "🧬" + " Native support: " + documentation.nativeSupport()
        + print(
        "  \n\n" + "⚙️" + " Parameters: \n\n"
            + "| Name | Aliases | Description | Default value |\n"
            + "| --- | --- | --- | --- |\n",
        Arrays
            .stream(documentation.parameters())
            .map(DocMarkdownFormatter::format)
            .collect(Collectors.joining("\n"))
    )
        + print(
        "  \n\n" + "💡" + " Example: \n```\n",
        documentation.example(),
        "```"
    );
  }

  private static String format(ParameterRecord parameter) {
    return "| " + parameter.name()
        + " | " + String.join(", ", parameter.aliases())
        + " | " + parameter.description()
        + " | " + parameter.defaultValue()
        + " |";
  }

  private static String print(String header, String value) {
    return print(header, value, "");
  }

  private static String print(String header, String value, String footer) {
    return value.isEmpty() ? "" : header + value + footer;
  }

  private static String icons(Boolean nativeSupport) {
    return " ♨️" + (nativeSupport ? " 🧬" : "");
  }
}
