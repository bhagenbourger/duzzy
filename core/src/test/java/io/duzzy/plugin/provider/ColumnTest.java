package io.duzzy.plugin.provider;

import io.duzzy.core.provider.Provider;

import static io.duzzy.core.column.ColumnContext.DEFAULT;
import static org.assertj.core.api.Assertions.assertThat;

public class ColumnTest {

    public static void checkSometimesNull(Provider<?> provider, int expected) {
        int cpt = 0;
        for (int i = 0; i < 1000; i++) {
            if (provider.value(DEFAULT) == null) {
                cpt++;
            }
        }
        assertThat(Math.round(cpt / 100f)).isEqualTo(expected);
    }
}
