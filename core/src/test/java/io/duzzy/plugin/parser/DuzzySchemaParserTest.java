package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
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
        final DuzzyContext duzzyContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzyContext).isInstanceOf(DuzzyContext.class);
        assertThat(duzzyContext.columns()).hasSize(1);
        assertThat(duzzyContext.columns().getFirst().name()).isEqualTo("city");
        assertThat(duzzyContext.columns().getFirst().columnType()).isEqualTo(ColumnType.STRING);
        assertThat(duzzyContext.columns().getFirst().nullRate()).isEqualTo(0f);
        assertThat(duzzyContext.columns().getFirst().providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
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
        assertThat(duzzyContext.columns()).hasSize(1);
        assertThat(duzzyContext.columns().getFirst().name()).isEqualTo("city");
        assertThat(duzzyContext.columns().getFirst().columnType()).isEqualTo(ColumnType.STRING);
        assertThat(duzzyContext.columns().getFirst().nullRate()).isEqualTo(0f);
        assertThat(duzzyContext.columns().getFirst().providers().getFirst()).isInstanceOf(AlphanumericRandomProvider.class);
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
        assertThat(duzzyContext.columns()).hasSize(1);
        assertThat(duzzyContext.columns().getFirst().value(DEFAULT)).isNotNull();
    }

    @Test
    void parsedFromYamlShouldBeAbleToReadSnakeCase() throws IOException {
        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-snake-case.yaml");
        final DuzzyContext duzzyContext = new DuzzySchemaParser().parse(duzzySchemaFile, null);

        assertThat(duzzyContext).isInstanceOf(DuzzyContext.class);
        assertThat(duzzyContext.columns()).hasSize(1);
        assertThat(duzzyContext.columns().getFirst().value(DEFAULT)).isNotNull();
    }
}
