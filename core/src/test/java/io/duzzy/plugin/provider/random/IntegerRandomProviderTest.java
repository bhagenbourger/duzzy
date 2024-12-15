package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.RANDOM_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegerRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/integer-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(IntegerRandomProvider.class);
        assertThat((Integer) provider.value(SEEDED_ONE_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(0)
                .isLessThanOrEqualTo(150);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/integer-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(IntegerRandomProvider.class);
        assertThat((Integer) provider.value(SEEDED_ONE_COLUMN_CONTEXT.get()))
                .isGreaterThanOrEqualTo(Integer.MIN_VALUE)
                .isLessThanOrEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void computeValueIsIdempotent() {
        final Integer value = new IntegerRandomProvider()
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(-1155869325);
    }

    @Test
    void computeValueWithRightRange() {
        final Integer value = new IntegerRandomProvider(5, 6)
                .value(RANDOM_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(5);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Integer value = new IntegerRandomProvider(3, 10)
                .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(-1155869325);
    }
}