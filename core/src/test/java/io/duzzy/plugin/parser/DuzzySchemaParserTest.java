package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzySchema;
import io.duzzy.plugin.column.random.AlphanumericRandomColumn;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class DuzzySchemaParserTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-full.yaml");
        final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzySchema).isInstanceOf(DuzzySchema.class);
        assertThat(duzzySchema.columns()).hasSize(1);
        assertThat(duzzySchema.columns().getFirst()).isInstanceOf(AlphanumericRandomColumn.class);
        assertThat(duzzySchema.sink()).isInstanceOf(ConsoleSink.class);
        assertThat(duzzySchema.sink().getSerializer()).isInstanceOf(JsonSerializer.class);
        assertThat(duzzySchema.rows()).isEqualTo(42L);
        assertThat(duzzySchema.seed()).isEqualTo(1234L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema.yaml");
        final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzySchema).isInstanceOf(DuzzySchema.class);
        assertThat(duzzySchema.columns()).hasSize(1);
        assertThat(duzzySchema.columns().getFirst()).isInstanceOf(AlphanumericRandomColumn.class);
        assertThat(duzzySchema.sink()).isInstanceOf(ConsoleSink.class);
        assertThat(duzzySchema.sink().getSerializer()).isInstanceOf(JsonSerializer.class);
        assertThat(duzzySchema.rows()).isEqualTo(10L);
        assertThat(duzzySchema.seed()).isNotNull();
    }

    @Test
    void parsedFromYamlShouldBeCaseInsensitive() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-case-insensitive.yaml");
        final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzySchema).isInstanceOf(DuzzySchema.class);
        assertThat(duzzySchema.columns()).hasSize(1);
        assertThat(duzzySchema.columns().getFirst().value(DEFAULT)).isNull();
    }

    @Test
    void parsedFromYamlShouldBeAbleToReadSnakeCase() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-snake-case.yaml");
        final DuzzySchema duzzySchema = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzySchema).isInstanceOf(DuzzySchema.class);
        assertThat(duzzySchema.columns()).hasSize(1);
        assertThat(duzzySchema.columns().getFirst().value(DEFAULT)).isNull();
    }
}
