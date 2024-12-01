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

public class DoubleIncrementProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/double-increment-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(DoubleIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(100.5d);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/double-increment-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(DoubleIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(0.1d);
    }

    @Test
    void computeValueWithRightStartingValue() {
        final Double value = new DoubleIncrementProvider(100d, 0.5d)
                .value(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100d);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        final Double value = new DoubleIncrementProvider(100d, 0.5d)
                .value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(100.5d);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Double value = new DoubleIncrementProvider(3d, 0.1d)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(1.3138947058478963E308);
    }
}
