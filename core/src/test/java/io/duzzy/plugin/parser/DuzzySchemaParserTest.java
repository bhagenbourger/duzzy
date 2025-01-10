package io.duzzy.plugin.parser;

import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DuzzySchemaParserTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-full.yaml");
    final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(duzzySchema.fields()).hasSize(1);
    final Field first = duzzySchema.fields().getFirst();
    assertThat(first.name()).isEqualTo("city");
    assertThat(first.type()).isEqualTo(Type.STRING);
    assertThat(first.nullRate()).isEqualTo(0f);
    assertThat(first.providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema.yaml");
    final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(duzzySchema.fields()).hasSize(1);
    final Field first = duzzySchema.fields().getFirst();
    assertThat(first.name()).isEqualTo("city");
    assertThat(first.type()).isEqualTo(Type.STRING);
    assertThat(first.nullRate()).isEqualTo(0f);
    assertThat(first.providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
  }

  @Test
  void parsedFromYamlShouldBeCaseInsensitive() throws IOException {
    final File duzzySchemaFile =
        getFromResources(getClass(), "schema/duzzy-schema-case-insensitive.yaml");
    final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(duzzySchema.fields()).hasSize(1);
    assertThat(duzzySchema.fields().getFirst().value(RANDOM_FIELD_CONTEXT.get())).isNotNull();
  }

  @Test
  void parsedFromYamlShouldBeAbleToReadSnakeCase() throws IOException {
    final File duzzySchemaFile =
        getFromResources(getClass(), "schema/duzzy-schema-snake-case.yaml");
    final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

    assertThat(duzzySchema.fields()).hasSize(1);
    assertThat(duzzySchema.fields().getFirst().value(RANDOM_FIELD_CONTEXT.get())).isNotNull();
  }
}
