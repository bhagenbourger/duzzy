package io.duzzy.plugin.provider.random;

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

public class BooleanRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/boolean-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(BooleanRandomProvider.class);
        assertThat((Boolean) provider.value(DUMMY_COLUMN_CONTEXT.get())).isTrue();
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/boolean-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(BooleanRandomProvider.class);
        assertThat((Boolean) provider.value(DUMMY_COLUMN_CONTEXT.get())).isTrue();
    }

    @Test
    void computeValueIsIdempotent() {
        Boolean value = new BooleanRandomProvider().value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isTrue();
    }
}
