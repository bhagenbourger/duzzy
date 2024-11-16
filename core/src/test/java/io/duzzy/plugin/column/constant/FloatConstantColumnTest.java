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

public class FloatConstantColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/float-constant-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(FloatConstantColumn.class);
        assertThat(column.getName()).isEqualTo("unit");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1f);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/float-constant-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(FloatConstantColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        Float value = new FloatConstantColumn("", null, null, 3f).value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(3f);
    }

    @Test
    void computeValueIsConstant() {
        Float value = new FloatConstantColumn("", null, null, 3f).value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo(3f);
    }

    @Test
    void valueIsAlwaysNull() {
        Float value = new FloatConstantColumn("", null, 1f, 3f).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Float value = new FloatConstantColumn("", null, 0f, 3f).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new FloatConstantColumn("", null, 0.5f, 3f), 5);
    }
}
