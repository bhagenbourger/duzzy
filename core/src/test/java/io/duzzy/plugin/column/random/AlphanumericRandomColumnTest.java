package io.duzzy.plugin.column.random;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.plugin.column.ColumnTest.checkSometimesNull;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class AlphanumericRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/alphanumeric-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(AlphanumericRandomColumn.class);
        assertThat(column.getName()).isEqualTo("city");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get()).toString().length()).isBetween(6, 8);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/alphanumeric-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(AlphanumericRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get()).toString()).hasSizeBetween(10, 15);
    }

    @Test
    void computeValueIsIdempotent() {
        String value = new AlphanumericRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("FoM4kOLyCysoR");
    }

    @Test
    void computeValueWithRightLength() {
        String value = new AlphanumericRandomColumn("", null, null, 5, 5).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).hasSize(5);
    }

    @Test
    void valueIsAlwaysNull() {
        String value = new AlphanumericRandomColumn("", null, 1f, 5, 5).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        String value = new AlphanumericRandomColumn("", null, 0f, 5, 5).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new AlphanumericRandomColumn("", null, 0.5f, 5, 5), 5);
    }
}
