package io.duzzy.plugin.column.constant;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.plugin.column.ColumnTest.checkSometimesNull;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class StringListConstantColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "column/constant/string-constant-list-column-full.yaml"
        );
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(StringListConstantColumn.class);
        assertThat(column.getName()).isEqualTo("unit");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo("two");
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "column/constant/string-constant-list-column.yaml"
        );
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(StringListConstantColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo("constant");
    }

    @Test
    void computeValueIsIdempotent() {
        String value = new StringListConstantColumn("", null, null, List.of("one", "two", "three"))
                .value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("two");
    }

    @Test
    void computeValueIsInConstants() {
        String value = new StringListConstantColumn("", null, null, List.of("one", "two", "three"))
                .value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo("two");
    }

    @Test
    void valueIsAlwaysNull() {
        String value = new StringListConstantColumn("", null, 1f, List.of("one", "two", "three")).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        String value = new StringListConstantColumn("", null, 0f, List.of("one", "two", "three")).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new StringListConstantColumn("", null, 0.5f, List.of("one", "two", "three")), 5);
    }
}
