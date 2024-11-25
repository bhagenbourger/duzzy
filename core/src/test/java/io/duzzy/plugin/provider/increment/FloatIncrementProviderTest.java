package io.duzzy.plugin.provider.increment;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class FloatIncrementProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/float-increment-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(FloatIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(100.5f);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/float-increment-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(FloatIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(0.1f);
    }

    @Test
    void computeValueWithRightStartingValue() {
        final Float value = new FloatIncrementProvider(100f, 0.5f)
                .value(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100f);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        final Float value = new FloatIncrementProvider(100f, 0.5f)
                .value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(100.5f);
    }
}
