package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.plugin.provider.increment.LongIncrementProvider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class LongRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/long-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongRandomProvider.class);
        assertThat((Long) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(0L)
                .isLessThanOrEqualTo(150L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/long-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongRandomProvider.class);
        assertThat((Long) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Long.MIN_VALUE)
                .isLessThanOrEqualTo(Long.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Long value = new LongRandomProvider().value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-4964420948893066024L);
    }

    @Test
    void computeValueWithRightRange() {
        Long value = new LongRandomProvider(5L, 6L).value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(5L);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Long value = new LongRandomProvider(3L, 4L)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-4964420948893066024L);
    }
}
