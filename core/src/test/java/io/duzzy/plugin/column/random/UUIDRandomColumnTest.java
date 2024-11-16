package io.duzzy.plugin.column.random;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.plugin.column.ColumnTest.checkSometimesNull;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class UUIDRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/uuid-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(UUIDRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/uuid-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(UUIDRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
    }

    @Test
    void computeValueIsIdempotent() {
        UUID value = new UUIDRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(UUID.fromString("3d1c63a5-7e66-3d0e-87e7-6e21796eed1f"));
    }

    @Test
    void valueIsAlwaysNull() {
        UUID value = new UUIDRandomColumn("", 1f).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        UUID value = new UUIDRandomColumn("", 0f).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new UUIDRandomColumn("", 0.5f), 5);
    }
}
