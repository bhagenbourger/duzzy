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

public class DoubleRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/double-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(DoubleRandomColumn.class);
        assertThat(column.getName()).isEqualTo("weight");
        assertThat((Double) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(50d)
                .isLessThanOrEqualTo(100d);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/double-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(DoubleRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Double) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Double.MIN_VALUE)
                .isLessThanOrEqualTo(Double.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Double value = new DoubleRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(1.3138947058478963E308);
    }

    @Test
    void computeValueWithRightRange() {
        Double value = new DoubleRandomColumn("", null, null, 5d, 6d).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isGreaterThanOrEqualTo(5d);
        assertThat(value).isLessThan(6d);
    }

    @Test
    void valueIsAlwaysNull() {
        Double value = new DoubleRandomColumn("", null, 1f, 5d, 6d).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Double value = new DoubleRandomColumn("", null, 0f, 5d, 6d).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new DoubleRandomColumn("", null, 0.5f, 5d, 6d), 5);
    }
}
