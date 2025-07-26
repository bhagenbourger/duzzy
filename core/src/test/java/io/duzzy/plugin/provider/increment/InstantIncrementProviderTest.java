package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class InstantIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/instant-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(InstantIncrementProvider.class);
    assertThat((Instant) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(Instant.parse("2023-01-02T00:00:00Z"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/instant-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(InstantIncrementProvider.class);
    assertThat((Instant) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Instant value = new InstantIncrementProvider("2023-01-01T00:00:00Z", 1L, "SECONDS")
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Instant.parse("2023-01-01T00:00:01Z"));
  }

  @Test
  void computeValueWithRightRange() {
    final Instant value = new InstantIncrementProvider("2023-01-01T00:00:00Z", 1L, "DAYS")
        .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(Instant.parse("2023-01-06T00:00:00Z"));
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Instant value = new InstantIncrementProvider("2023-01-01T00:00:00Z", 1L, "SECONDS")
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Instant.ofEpochMilli(-4964420948893066024L));
  }
}
