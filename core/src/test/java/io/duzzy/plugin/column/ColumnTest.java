package io.duzzy.plugin.column;

import io.duzzy.core.column.Column;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class ColumnTest {

    public static void checkSometimesNull(Column<?> column, int expected) {
        int cpt = 0;
        for (int i = 0; i < 1000; i++) {
            if (column.value(DEFAULT) == null) {
                cpt++;
            }
        }
        assertThat(Math.round(cpt / 100f)).isEqualTo(expected);
    }
}
