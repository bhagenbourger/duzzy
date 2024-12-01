package io.duzzy.plugin.provider.increment;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class LongIncrementProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/long-increment-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(110L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/long-increment-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1L);
    }

    @Test
    void computeValueWithRightStartingValue() {
        final Long value = new LongIncrementProvider(100L, 10L)
                .value(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100L);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        final Long value = new LongIncrementProvider(100L, 10L)
                .value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(110L);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Long value = new LongIncrementProvider(3L, 1L)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-4964420948893066024L);
    }
}
