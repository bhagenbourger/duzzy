package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegerRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/integer-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(IntegerRandomProvider.class);
        assertThat((Integer) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(0)
                .isLessThanOrEqualTo(150);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/integer-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(IntegerRandomProvider.class);
        assertThat((Integer) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Integer.MIN_VALUE)
                .isLessThanOrEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        Integer value = new IntegerRandomProvider().value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-1155869325);
    }

    @Test
    void computeValueWithRightRange() {
        Integer value = new IntegerRandomProvider(5, 6).value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).isEqualTo(5);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Integer value = new IntegerRandomProvider(3, 10)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(-1155869325);
    }
}
