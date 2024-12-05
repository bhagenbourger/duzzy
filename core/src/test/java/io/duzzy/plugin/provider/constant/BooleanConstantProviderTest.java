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

public class BooleanConstantProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/constant/boolean-constant-provider-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(BooleanConstantProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(false);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/constant/boolean-constant-provider.yaml");
        final BooleanConstantProvider column = YAML_MAPPER.readValue(columnFile, BooleanConstantProvider.class);

        assertThat(column.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        final Boolean value = new BooleanConstantProvider(true)
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isTrue();
    }

    @Test
    void corruptedValueIsIdempotent() {
        final Boolean value = new BooleanConstantProvider(true)
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isTrue();
    }

    @Test
    void corruptedValueIsConstant() {
        final Boolean value = new BooleanConstantProvider(true)
                .corruptedValue(SEEDED_FIVE_COLUMN_CONTEXT.get());
        assertThat(value).isFalse();
    }
}
