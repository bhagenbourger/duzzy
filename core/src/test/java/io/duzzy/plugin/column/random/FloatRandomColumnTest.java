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

public class FloatRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/float-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(FloatRandomColumn.class);
        assertThat(column.getName()).isEqualTo("weight");
        assertThat((Float) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(50f)
                .isLessThanOrEqualTo(100f);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/float-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(FloatRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Float) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Float.MIN_VALUE)
                .isLessThanOrEqualTo(Float.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Float value = new FloatRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(2.4870493E38F);
    }

    @Test
    void computeValueWithRightRange() {
        Float value = new FloatRandomColumn("", null, null, 5f, 6f).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isGreaterThanOrEqualTo(5f);
        assertThat(value).isLessThan(6f);
    }

    @Test
    void valueIsAlwaysNull() {
        Float value = new FloatRandomColumn("", null, 1f, 5f, 6f).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Float value = new FloatRandomColumn("", null, 0f, 5f, 6f).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new FloatRandomColumn("", null, 0.5f, 5f, 6f), 5);
    }
}
