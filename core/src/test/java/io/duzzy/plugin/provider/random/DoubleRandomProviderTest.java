package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.plugin.provider.increment.DoubleIncrementProvider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class DoubleRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/double-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(DoubleRandomProvider.class);
        assertThat((Double) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(50d)
                .isLessThanOrEqualTo(100d);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/double-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(DoubleRandomProvider.class);
        assertThat((Double) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Double.MIN_VALUE)
                .isLessThanOrEqualTo(Double.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Double value = new DoubleRandomProvider().value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(1.3138947058478963E308);
    }

    @Test
    void computeValueWithRightRange() {
        Double value = new DoubleRandomProvider(5d, 6d).value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isGreaterThanOrEqualTo(5d);
        assertThat(value).isLessThan(6d);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Double value = new DoubleRandomProvider(3d, 4d)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(1.3138947058478963E308);
    }
}
