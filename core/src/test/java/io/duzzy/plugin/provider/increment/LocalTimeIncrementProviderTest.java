package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class LocalTimeIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/local-time-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalTimeIncrementProvider.class);
    assertThat((LocalTime) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(LocalTime.parse("01:00:00"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/local-time-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalTimeIncrementProvider.class);
    assertThat((LocalTime) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalTime value = new LocalTimeIncrementProvider("00:00:00", 1L, "SECONDS")
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalTime.parse("00:00:01"));
  }

  @Test
  void computeValueWithRightRange() {
    final LocalTime value = new LocalTimeIncrementProvider("00:00:00", 1L, "HOURS")
        .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(LocalTime.parse("05:00:00"));
  }
}
