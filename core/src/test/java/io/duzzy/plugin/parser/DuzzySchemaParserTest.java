package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnType;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.sink.ConsoleSink;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.test.TestUtility.RANDOM_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class DuzzySchemaParserTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-full.yaml");
        final DuzzyContext duzzyContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzyContext).isInstanceOf(DuzzyContext.class);
        assertThat(duzzyContext.schemaContext().columns()).hasSize(1);
        final Column first = duzzyContext.schemaContext().columns().getFirst();
        assertThat(first.name()).isEqualTo("city");
        assertThat(first.columnType()).isEqualTo(ColumnType.STRING);
        assertThat(first.nullRate()).isEqualTo(0f);
        assertThat(first.providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
        assertThat(duzzyContext.sink()).isInstanceOf(ConsoleSink.class);
        assertThat(duzzyContext.sink().getSerializer()).isInstanceOf(JsonSerializer.class);
        assertThat(duzzyContext.rows()).isEqualTo(42L);
        assertThat(duzzyContext.seed()).isEqualTo(1234L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema.yaml");
        final DuzzyContext duzzyContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzyContext).isInstanceOf(DuzzyContext.class);
        assertThat(duzzyContext.schemaContext().columns()).hasSize(1);
        final Column first = duzzyContext.schemaContext().columns().getFirst();
        assertThat(first.name()).isEqualTo("city");
        assertThat(first.columnType()).isEqualTo(ColumnType.STRING);
        assertThat(first.nullRate()).isEqualTo(0f);
        assertThat(first.providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
        assertThat(duzzyContext.sink()).isInstanceOf(ConsoleSink.class);
        assertThat(duzzyContext.sink().getSerializer()).isInstanceOf(JsonSerializer.class);
        assertThat(duzzyContext.rows()).isEqualTo(10L);
        assertThat(duzzyContext.seed()).isNotNull();
    }

    @Test
    void parsedFromYamlShouldBeCaseInsensitive() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-case-insensitive.yaml");
        final DuzzyContext duzzyContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzyContext).isInstanceOf(DuzzyContext.class);
        assertThat(duzzyContext.schemaContext().columns()).hasSize(1);
        assertThat(duzzyContext.schemaContext().columns().getFirst().value(RANDOM_COLUMN_CONTEXT.get())).isNotNull();
    }

    @Test
    void parsedFromYamlShouldBeAbleToReadSnakeCase() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-snake-case.yaml");
        final DuzzyContext duzzyContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzyContext).isInstanceOf(DuzzyContext.class);
        assertThat(duzzyContext.schemaContext().columns()).hasSize(1);
        assertThat(duzzyContext.schemaContext().columns().getFirst().value(RANDOM_COLUMN_CONTEXT.get())).isNotNull();
    }
}
