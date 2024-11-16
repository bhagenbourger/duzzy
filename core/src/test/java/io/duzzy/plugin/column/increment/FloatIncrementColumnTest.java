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

public class FloatIncrementColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/increment/float-increment-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(FloatIncrementColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(100.5f);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/increment/float-increment-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(FloatIncrementColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(0.1f);
    }

    @Test
    void computeValueWithRightStartingValue() {
        Float value = new FloatIncrementColumn("", null,null, 100f, 0.5f).computeValue(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100f);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        Float value = new FloatIncrementColumn("", null,null, 100f, 0.5f).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(100.5f);
    }

    @Test
    void valueIsAlwaysNull() {
        Float value = new FloatIncrementColumn("", null,1f, 100f, 0.5f).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Float value = new FloatIncrementColumn("", null,0f, 100f, 0.5f).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new FloatIncrementColumn("", null,0.5f, 100f, 0.5f), 5);
    }
}
