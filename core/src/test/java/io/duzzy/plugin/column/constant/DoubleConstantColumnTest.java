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

public class DoubleConstantColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/double-constant-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(DoubleConstantColumn.class);
        assertThat(column.getName()).isEqualTo("unit");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1d);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/double-constant-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(DoubleConstantColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        Double value = new DoubleConstantColumn("", null,null, 3d).value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(3d);
    }

    @Test
    void computeValueIsConstant() {
        Double value = new DoubleConstantColumn("", null,null, 3d).value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo(3d);
    }


    @Test
    void valueIsAlwaysNull() {
        Double value = new DoubleConstantColumn("", null,1f, 3d).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Double value = new DoubleConstantColumn("", null,0f, 3d).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new DoubleConstantColumn("", null,0.5f, 3d), 5);
    }
}
