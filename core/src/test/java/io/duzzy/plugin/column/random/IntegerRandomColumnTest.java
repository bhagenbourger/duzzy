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

public class IntegerRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/integer-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(IntegerRandomColumn.class);
        assertThat(column.getName()).isEqualTo("age");
        assertThat((Integer) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(0)
                .isLessThanOrEqualTo(150);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/integer-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(IntegerRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Integer) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Integer.MIN_VALUE)
                .isLessThanOrEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Integer value = new IntegerRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-1155869325);
    }

    @Test
    void computeValueWithRightRange() {
        Integer value = new IntegerRandomColumn("", null, null, 5, 6).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(5);
    }

    @Test
    void valueIsAlwaysNull() {
        Integer value = new IntegerRandomColumn("", null, 1f, 5, 6).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Integer value = new IntegerRandomColumn("", null, 0f, 5, 6).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new IntegerRandomColumn("", null, 0.5f, 5, 6), 5);
    }
}
