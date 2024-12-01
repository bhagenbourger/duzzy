package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.plugin.provider.constant.StringConstantProvider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class AlphanumericRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/alphanumeric-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(AlphanumericRandomProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get()).toString().length()).isBetween(6, 8);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/alphanumeric-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(AlphanumericRandomProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get()).toString()).hasSizeBetween(10, 15);
    }

    @Test
    void computeValueIsIdempotent() {
        String value = new AlphanumericRandomProvider().value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("FoM4kOLyCysoR");
    }

    @Test
    void computeValueWithRightLength() {
        String value = new AlphanumericRandomProvider(5, 5).value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value).hasSize(5);
    }

    @Test
    void corruptedValueIsIdempotentAndRespectMaxLength() {
        final String value = new AlphanumericRandomProvider(5, 15)
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("Od`");
        assertThat(value).hasSizeBetween(0, 15);
    }
}
