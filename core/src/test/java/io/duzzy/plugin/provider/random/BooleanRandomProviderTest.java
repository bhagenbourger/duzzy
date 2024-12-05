package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.SEEDED_FIVE_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class BooleanRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/boolean-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(BooleanRandomProvider.class);
        assertThat((Boolean) provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isTrue();
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/boolean-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(BooleanRandomProvider.class);
        assertThat((Boolean) provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isTrue();
    }

    @Test
    void computeValueIsIdempotent() {
        final Boolean value = new BooleanRandomProvider()
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isTrue();
    }

    @Test
    void corruptedValueIsConstant() {
        final Boolean value = new BooleanRandomProvider()
                .corruptedValue(SEEDED_FIVE_COLUMN_CONTEXT.get());
        assertThat(value).isTrue();
    }
}
