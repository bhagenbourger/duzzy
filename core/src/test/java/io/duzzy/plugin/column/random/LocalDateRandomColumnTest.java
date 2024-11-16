package io.duzzy.plugin.column.random;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.plugin.column.ColumnTest.checkSometimesNull;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LocalDateRandomColumnTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/local-date-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(LocalDateRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((LocalDate) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isAfterOrEqualTo(LocalDate.of(2020, 5, 26))
                .isBefore(LocalDate.of(2020, 5, 27));
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/local-date-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(LocalDateRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((LocalDate) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isAfterOrEqualTo(LocalDate.MIN)
                .isBeforeOrEqualTo(LocalDate.MAX);
    }

    @Test
    void errorWhenMaxIsBeforeMin(){
        assertThatThrownBy(() -> new LocalDateRandomColumn("error", null, "2020-10-10", "2000-01-01"))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Min local date must be before max local date in colum error");
    }

    @Test
    void computeValueIsIdempotent() {
        LocalDate value = new LocalDateRandomColumn("", null, null, null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("2014-04-07");
    }

    @Test
    void computeValueWithRightRange() {
        LocalDate value = new LocalDateRandomColumn("", null, "2020-05-30", "2021-05-30").computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value)
                .isAfterOrEqualTo("2020-05-30")
                .isBeforeOrEqualTo("2021-05-30");
    }

    @Test
    void valueIsAlwaysNull() {
        LocalDate value = new LocalDateRandomColumn("", 1f, "2020-05-30", "2021-05-30").value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        LocalDate value = new LocalDateRandomColumn("", 0f, "2020-05-30", "2021-05-30").value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new LocalDateRandomColumn("", 0.5f, "2020-05-30", "2021-05-30"), 5);
    }
}
