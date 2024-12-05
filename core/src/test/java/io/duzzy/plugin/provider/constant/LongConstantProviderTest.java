package io.duzzy.plugin.provider.constant;

import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.SEEDED_FIVE_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class LongConstantProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/constant/long-constant-provider-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongConstantProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(1L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/constant/long-constant-provider.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongConstantProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        final Long value = new LongConstantProvider(3L).value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(3L);
    }

    @Test
    void computeValueIsConstant() {
        final Long value = new LongConstantProvider(3L).value(SEEDED_FIVE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(3L);
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Long value = new LongConstantProvider(3L).corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(-4964420948893066024L);
    }
}
