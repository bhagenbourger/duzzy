package io.duzzy.cli.doc;

import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DocMarkdownFormatter {

    public static String format(Map<DuzzyType, List<Documentation>> doc) {
        return "# Duzzy components" + "\n" +
                doc
                        .keySet()
                        .stream()
                        .sorted()
                        .map(e -> format(e, doc.get(e)))
                        .collect(Collectors.joining("\n")) + FOOTER;
    }

    private static String format(DuzzyType duzzyType, List<Documentation> documentations) {
        return "\n## " + duzzyType.name() + "\n" +
                documentations
                        .stream()
                        .map(DocMarkdownFormatter::format)
                        .collect(Collectors.joining("\n"));
    }

    private static String format(Documentation documentation) {
        return "### " + documentation.identifier() +
                "\nIdentifier: " + documentation.identifier() +
                "\nDescription: " + documentation.description() +
                "\nExample: \n" + documentation.example() +
                print(
                        "\nParameters: \n",
                        Arrays
                                .stream(documentation.parameters())
                                .map(DocMarkdownFormatter::format)
                                .collect(Collectors.joining("\n"))
                );
    }

    private static String format(Parameter parameter) {
        return "  - Name: " + parameter.name() +
                print("\n    Aliases: ", String.join(", ", parameter.aliases())) +
                "\n    Description: " + parameter.description() +
                print("\n    Default value: ", parameter.defaultValue());
    }

    private static String print(String key, String value) {
        return value.isEmpty() ? "" : key + value;
    }

    private static final String FOOTER = "\n\nQualified name of component " +
            "must contains \"duzzy\" " +
            "to appear  into this documentation";
}
