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

public class BooleanConstantColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/boolean-constant-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(BooleanConstantColumn.class);
        assertThat(column.getName()).isEqualTo("unit");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(false);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/constant/boolean-constant-column.yaml");
        final BooleanConstantColumn column = YAML_MAPPER.readValue(columnFile, BooleanConstantColumn.class);

        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        Boolean value = new BooleanConstantColumn("", null, null, true)
                .value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isTrue();
    }

    @Test
    void computeValueIsConstant() {
        Boolean value = new BooleanConstantColumn("", null, null, true)
                .value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isTrue();
    }

    @Test
    void valueIsAlwaysNull() {
        Boolean value = new BooleanConstantColumn("", null, 1f, true).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Boolean value = new BooleanConstantColumn("", null, 0f, true).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new BooleanConstantColumn("", null, 0.5f, true), 5);
    }
}
