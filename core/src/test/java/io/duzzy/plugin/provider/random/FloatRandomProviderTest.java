package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.plugin.provider.increment.FloatIncrementProvider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class FloatRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/float-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(FloatRandomProvider.class);
        assertThat((Float) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(50f)
                .isLessThanOrEqualTo(100f);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/float-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(FloatRandomProvider.class);
        assertThat((Float) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Float.MIN_VALUE)
                .isLessThanOrEqualTo(Float.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Float value = new FloatRandomProvider().value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(2.4870493E38F);
    }

    @Test
    void computeValueWithRightRange() {
        Float value = new FloatRandomProvider(5f, 6f).value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isGreaterThanOrEqualTo(5f);
        assertThat(value).isLessThan(6f);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Float value = new FloatRandomProvider(3f, 4f)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(2.4870493E38f);
    }
}
