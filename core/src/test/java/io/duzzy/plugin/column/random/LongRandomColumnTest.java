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

public class LongRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/long-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(LongRandomColumn.class);
        assertThat(column.getName()).isEqualTo("age");
        assertThat((Long) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(0L)
                .isLessThanOrEqualTo(150L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/long-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(LongRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Long) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Long.MIN_VALUE)
                .isLessThanOrEqualTo(Long.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Long value = new LongRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-4964420948893066024L);
    }

    @Test
    void computeValueWithRightRange() {
        Long value = new LongRandomColumn("", null, null, 5L, 6L).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(5L);
    }

    @Test
    void valueIsAlwaysNull() {
        Long value = new LongRandomColumn("", null, 1f, 5L, 6L).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Long value = new LongRandomColumn("", null, 0f, 5L, 6L).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new LongRandomColumn("", null, 0.5f, 5L, 6L), 5);
    }
}
