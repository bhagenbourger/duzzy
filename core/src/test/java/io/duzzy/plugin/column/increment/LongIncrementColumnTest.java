package io.duzzy.plugin.column.increment;

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

public class LongIncrementColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/increment/long-increment-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(LongIncrementColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(110L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/increment/long-increment-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(LongIncrementColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1L);
    }

    @Test
    void computeValueWithRightStartingValue() {
        Long value = new LongIncrementColumn("", null, null, 100L, 10L).computeValue(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100L);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        Long value = new LongIncrementColumn("", null, null, 100L, 10L).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(110L);
    }

    @Test
    void valueIsAlwaysNull() {
        Long value = new LongIncrementColumn("", null, 1f, 100L, 10L).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Long value = new LongIncrementColumn("", null, 0f, 100L, 10L).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new LongIncrementColumn("", null, 0.5f, 100L, 10L), 5);
    }
}
