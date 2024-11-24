package io.duzzy.core.documentation;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.util.Locale.ROOT;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DuzzyDoc {

  public static final ObjectMapper MAPPER = new ObjectMapper();
  public static final String FILENAME = "duzzy-doc.json";

  /**
   * Generates a map of DuzzyType to a list of DocumentationRecord from the JSON file.
   *
   * @return a map where keys are DuzzyType and values are lists of DocumentationRecord
   * @throws IOException if there is an error reading the JSON file
   */
  public static Map<DuzzyType, List<DocumentationRecord>> generate(
      DuzzyType duzzyType,
      String duzzyIdentifier,
      String module
  ) throws IOException {
    final Enumeration<URL> resources = getSystemClassLoader().getResources(FILENAME);
    final List<DocumentationRecord> documentations = new ArrayList<>();
    while (resources.hasMoreElements()) {
      final URL resource = resources.nextElement();
      try (final InputStream resourceAsStream = resource.openStream()) {
        final List<DocumentationRecord> docs = MAPPER.readValue(
            resourceAsStream,
            MAPPER.getTypeFactory().constructCollectionType(List.class, DocumentationRecord.class)
        );
        documentations.addAll(docs);
      }
    }

    return documentations.stream()
        .filter(predicate(duzzyType, duzzyIdentifier, module))
        .collect(Collectors.groupingBy(DocumentationRecord::duzzyType));
  }

  private static Predicate<DocumentationRecord> predicate(
      DuzzyType duzzyType,
      String duzzyIdentifier,
      String module
  ) {
    Predicate<DocumentationRecord> predicate = d -> true;
    if (duzzyType != null) {
      predicate = predicate.and(d -> d.duzzyType() == duzzyType);
    }
    if (duzzyIdentifier != null && !duzzyIdentifier.isEmpty()) {
      predicate = predicate.and(
          d -> d.identifier().toLowerCase(ROOT).contains(duzzyIdentifier.toLowerCase(ROOT))
      );
    }
    if (module != null && !module.isEmpty()) {
      predicate = predicate.and(
          d -> d.module().toLowerCase(ROOT).contains(module.toLowerCase(ROOT))
      );
    }
    return predicate;
  }

}
