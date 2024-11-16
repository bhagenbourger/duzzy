package io.duzzy.plugin.column.constant;

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

public class IntegerConstantColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/integer-constant-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(IntegerConstantColumn.class);
        assertThat(column.getName()).isEqualTo("unit");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/integer-constant-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(IntegerConstantColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        Integer value = new IntegerConstantColumn("", null, null, 3).value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(3);
    }

    @Test
    void computeValueIsConstant() {
        Integer value = new IntegerConstantColumn("", null, null, 3).value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo(3);
    }

    @Test
    void valueIsAlwaysNull() {
        Integer value = new IntegerConstantColumn("", null, 1f, 3).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Integer value = new IntegerConstantColumn("", null, 0f, 3).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new IntegerConstantColumn("", null, 0.5f, 3), 5);
    }
}
