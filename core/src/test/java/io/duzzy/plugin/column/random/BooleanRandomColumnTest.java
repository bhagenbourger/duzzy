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

public class BooleanRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/boolean-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(BooleanRandomColumn.class);
        assertThat(column.getName()).isEqualTo("Is ok ?");
        assertThat((Boolean) column.value(DUMMY_COLUMN_CONTEXT.get())).isFalse();
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/boolean-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(BooleanRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Boolean) column.value(DUMMY_COLUMN_CONTEXT.get())).isFalse();
    }

    @Test
    void computeValueIsIdempotent() {
        Boolean value = new BooleanRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isTrue();
    }

    @Test
    void valueIsAlwaysNull() {
        Boolean value = new BooleanRandomColumn("", null, 1f).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Boolean value = new BooleanRandomColumn("", null, 0f).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new BooleanRandomColumn("", null, 0.5f), 5);
    }
}
