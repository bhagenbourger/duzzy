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

public class IntegerIncrementProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/integer-increment-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(IntegerIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(110);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/increment/integer-increment-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(IntegerIncrementProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1);
    }

    @Test
    void computeValueWithRightStartingValue() {
        final Integer value = new IntegerIncrementProvider(100, 10)
                .value(new ColumnContext(new Random(), 1L, 0L));
        assertThat(value).isEqualTo(100);
    }

    @Test
    void computeValueWithRightFirstStepValue() {
        final Integer value = new IntegerIncrementProvider(100, 10)
                .value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(110);
    }
}
