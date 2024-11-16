package io.duzzy.plugin.column.random;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.plugin.column.ColumnTest.checkSometimesNull;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InstantRandomColumnTest {

    private static final String START = "2020-05-26T23:59:59.999999999Z";
    private static final String END = "2020-05-27T23:59:59.999999999Z";


    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/instant-random-column-full.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(InstantRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Instant) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isAfterOrEqualTo(Instant.parse(START))
                .isBefore(Instant.parse(END));
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "column/random/instant-random-column.yaml");
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(InstantRandomColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat((Instant) column.value(DUMMY_COLUMN_CONTEXT.get()))
                .isAfterOrEqualTo(Instant.MIN)
                .isBeforeOrEqualTo(Instant.MAX);
    }

    @Test
    void errorWhenMaxIsBeforeMin(){
        assertThatThrownBy(() -> new InstantRandomColumn("error", null, null, END, START))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Min instant must be before max instant in colum error");
    }

    @Test
    void computeValueIsIdempotent() {
        Instant value = new InstantRandomColumn("", null).computeValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("6836-01-05T14:44:02.796Z");
    }

    @Test
    void computeValueWithRightRange() {
        Instant value = new InstantRandomColumn("", null, null, START, END).computeValue(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value)
                .isAfterOrEqualTo(START)
                .isBeforeOrEqualTo(END);
    }

    @Test
    void valueIsAlwaysNull() {
        Instant value = new InstantRandomColumn("", null,1f, START, END).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        Instant value = new InstantRandomColumn("", null, 0f, START, END).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new InstantRandomColumn("", null, 0.5f, START, END), 5);
    }
}
