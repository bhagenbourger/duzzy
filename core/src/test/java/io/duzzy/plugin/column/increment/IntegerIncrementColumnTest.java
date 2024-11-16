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

public class IntegerIncrementColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/increment/integer-increment-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(IntegerIncrementColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(110);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/increment/integer-increment-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(IntegerIncrementColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1);
    }

    @Test
    void computeValueWithRightStartingValue() {
        Integer value = new IntegerIncrementColumn("", null, null, 100, 10).computeValue(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        Integer value = new IntegerIncrementColumn("", null, null, 100, 10).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(110);
    }

    @Test
    void valueIsAlwaysNull() {
        Integer value = new IntegerIncrementColumn("", null, 1f, 100, 10).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Integer value = new IntegerIncrementColumn("", null, 0f, 100, 10).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new IntegerIncrementColumn("", null, 0.5f, 100, 10), 5);
    }
}
