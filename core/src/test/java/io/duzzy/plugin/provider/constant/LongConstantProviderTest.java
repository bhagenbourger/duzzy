package io.duzzy.plugin.provider.constant;

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

public class LongConstantProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/constant/long-constant-provider-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongConstantProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo(1L);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/constant/long-constant-provider.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LongConstantProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isNull();
    }

    @Test
    void computeValueIsIdempotent() {
        final Long value = new LongConstantProvider(3L)
                .value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo(3L);
    }

    @Test
    void computeValueIsConstant() {
        final Long value = new LongConstantProvider(3L)
                .value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo(3L);
    }
}
