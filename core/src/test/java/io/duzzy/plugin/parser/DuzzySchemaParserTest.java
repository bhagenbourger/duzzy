package io.duzzy.plugin.parser;

import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DuzzySchemaParserTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-full.yaml");
    final SchemaContext schemaContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(schemaContext.fields()).hasSize(1);
    final Field first = schemaContext.fields().getFirst();
    assertThat(first.name()).isEqualTo("city");
    assertThat(first.type()).isEqualTo(Type.STRING);
    assertThat(first.nullRate()).isEqualTo(0f);
    assertThat(first.providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema.yaml");
    final SchemaContext schemaContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(schemaContext.fields()).hasSize(1);
    final Field first = schemaContext.fields().getFirst();
    assertThat(first.name()).isEqualTo("city");
    assertThat(first.type()).isEqualTo(Type.STRING);
    assertThat(first.nullRate()).isEqualTo(0f);
    assertThat(first.providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
  }

  @Test
  void parsedFromYamlShouldBeCaseInsensitive() throws IOException {
    final File duzzySchemaFile =
        getFromResources(getClass(), "schema/duzzy-schema-case-insensitive.yaml");
    final SchemaContext schemaContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(schemaContext.fields()).hasSize(1);
    assertThat(schemaContext.fields().getFirst().value(RANDOM_FIELD_CONTEXT.get())).isNotNull();
  }

  @Test
  void parsedFromYamlShouldBeAbleToReadSnakeCase() throws IOException {
    final File duzzySchemaFile =
        getFromResources(getClass(), "schema/duzzy-schema-snake-case.yaml");
    final SchemaContext schemaContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(schemaContext.fields()).hasSize(1);
    assertThat(schemaContext.fields().getFirst().value(RANDOM_FIELD_CONTEXT.get())).isNotNull();
  }
}
