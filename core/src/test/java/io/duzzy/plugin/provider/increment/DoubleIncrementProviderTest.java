package io.duzzy.plugin.provider.increment;

import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.RANDOM_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class DoubleIncrementProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/double-increment-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(DoubleIncrementProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(100.5d);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/double-increment-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(DoubleIncrementProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(0.1d);
    }

    @Test
    void computeValueWithRightStartingValue() {
        final Double value = new DoubleIncrementProvider(100d, 0.5d)
                .value(RANDOM_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(100d);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        final Double value = new DoubleIncrementProvider(100d, 0.5d)
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(100.5d);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Double value = new DoubleIncrementProvider(3d, 0.1d)
                .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(1.3138947058478963E308);
    }
}
