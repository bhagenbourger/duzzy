package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class UUIDRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/uuid-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(UUIDRandomProvider.class);
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/uuid-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(UUIDRandomProvider.class);
    }

    @Test
    void computeValueIsIdempotent() {
        final UUID value = new UUIDRandomProvider()
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(UUID.fromString("3d1c63a5-7e66-3d0e-87e7-6e21796eed1f"));
    }

    @Test
    void corruptedValueIsIdempotent() {
        final UUID value = new UUIDRandomProvider()
                .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo(UUID.fromString("3d1c63a5-7e66-3d0e-87e7-6e21796eed1f"));
    }
}