package io.duzzy.plugin.provider.constant;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class StringListConstantProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "provider/constant/string-constant-list-provider-full.yaml"
        );
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(StringListConstantProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo("one");
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "provider/constant/string-constant-list-provider.yaml"
        );
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(StringListConstantProvider.class);
        assertThat(provider.value(DUMMY_COLUMN_CONTEXT.get())).isEqualTo("constant");
    }

    @Test
    void computeValueIsIdempotent() {
        final String value = new StringListConstantProvider(List.of("one", "two", "three"))
                .value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("one");
    }

    @Test
    void computeValueIsInConstants() {
        final String value = new StringListConstantProvider(List.of("one", "two", "three"))
                .value(new ColumnContext(new Random(5L), 5L, 5L));
        assertThat(value).isEqualTo("three");
    }

    @Test
    void corruptedValueIsIdempotent() {
        final String value = new StringListConstantProvider(List.of("one", "two", "three"))
                .corruptedValue(new ColumnContext(new Random(10L), 1L, 1L));
        assertThat(value).isEqualTo("io");
    }
}
