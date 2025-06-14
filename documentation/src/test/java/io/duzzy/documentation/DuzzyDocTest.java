package io.duzzy.documentation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DuzzyDocTest {

  @Test
  void shouldGenerateDocumentation() throws IOException {
    final DocumentationRecord expected = new DocumentationRecord(
        "Unknown",
        "Serialize data in JSON",
        "Unknown",
        DuzzyType.UNKNOWN,
        false,
        null,
        null
    );
    final Map<DuzzyType, List<DocumentationRecord>> docs = DuzzyDoc.generate(
        null,
        null,
        null,
        false
    );
    final List<DocumentationRecord> serializers = docs.get(DuzzyType.SERIALIZER);
    final List<DocumentationRecord> unknown = docs.get(DuzzyType.UNKNOWN);
    final DocumentationRecord documentationRecord = unknown.getFirst();

    assertThat(docs.size()).isEqualTo(2);
    assertThat(serializers.size()).isEqualTo(3);
    assertThat(unknown.size()).isEqualTo(1);
    assertThat(documentationRecord).isEqualTo(expected);

  }
  
  @Test
  void shouldGenerateDocumentationFilterOnModule() throws IOException {
    final Map<DuzzyType, List<DocumentationRecord>> docs = DuzzyDoc.generate(
        null,
        null,
        "io.duzzy.core",
        false
    );
    final List<DocumentationRecord> records = docs.get(DuzzyType.SERIALIZER);
    
    assertThat(records.size()).isEqualTo(2);
  }

  @Test
  void shouldGenerateDocumentationFilterOnDuzzyType() throws IOException {
    final Map<DuzzyType, List<DocumentationRecord>> docs = DuzzyDoc.generate(
        DuzzyType.SERIALIZER,
        null,
        null,
        false
    );
    final List<DocumentationRecord> records = docs.get(DuzzyType.SERIALIZER);

    assertThat(records.size()).isEqualTo(3);
  }

  @Test
  void shouldGenerateDocumentationFilterOnIdentifier() throws IOException {
    final Map<DuzzyType, List<DocumentationRecord>> docs = DuzzyDoc.generate(
        null,
        "sql",
        null,
        false
    );
    final List<DocumentationRecord> records = docs.get(DuzzyType.SERIALIZER);

    assertThat(records.size()).isEqualTo(1);
  }

  @Test
  void shouldGenerateDocumentationFilterOnNativeSupport() throws IOException {
    final Map<DuzzyType, List<DocumentationRecord>> docs = DuzzyDoc.generate(
        null,
        null,
        null,
        true
    );
    final List<DocumentationRecord> records = docs.get(DuzzyType.SERIALIZER);

    assertThat(records.size()).isEqualTo(2);
  }
}
