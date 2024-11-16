package io.duzzy.plugin.column.constant;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.column.WeightedItem;
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

public class StringWeightedListConstantColumnTest {

    private static final List<WeightedItem<String>> VALUES = List.of(
            new WeightedItem<>("one", 1),
            new WeightedItem<>("two", 2),
            new WeightedItem<>("three", 3)
    );

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "column/constant/string-constant-weighted-list-column-full.yaml"
        );
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(StringWeightedListConstantColumn.class);
        assertThat(column.getName()).isEqualTo("unit");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo("three");
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "column/constant/string-constant-weighted-list-column.yaml"
        );
        final Column<?> column = YAML_MAPPER.readValue(columnFile, Column.class);

        assertThat(column).isInstanceOf(StringWeightedListConstantColumn.class);
        assertThat(column.getName()).isEqualTo("identifier");
        assertThat(column.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo("constant");
    }

    @Test
    void computeValueIsIdempotent() {
        String value = new StringWeightedListConstantColumn("", null, null, VALUES)
                .value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("three");
    }

    @Test
    void computeValueIsInConstants() {
        String value = new StringWeightedListConstantColumn("", null, null, VALUES)
                .value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo("three");
    }

    @Test
    void valueIsAlwaysNull() {
        String value = new StringWeightedListConstantColumn("", null, 1f, VALUES).value(DEFAULT);
        assertThat(value).isNull();
    }

    @Test
    void valueIsNeverNull() {
        String value = new StringWeightedListConstantColumn("", null, 0f, VALUES).value(DEFAULT);
        assertThat(value).isNotNull();
    }

    @Test
    void valueIsSometimeNull() {
        checkSometimesNull(new StringWeightedListConstantColumn("", null, 0.5f, VALUES), 5);
    }

    //TODO: test repartition
}
