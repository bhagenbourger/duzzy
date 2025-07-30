package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class LocalTimeRandomProviderTest {

  private static final String MIN_TIME = "10:00:00";
  private static final String MAX_TIME = "11:00:00";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/local-time-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalTimeRandomProvider.class);
    assertThat((LocalTime) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(LocalTime.parse(MIN_TIME))
        .isBefore(LocalTime.parse(MAX_TIME));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/local-time-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalTimeRandomProvider.class);
    assertThat((LocalTime) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(LocalTime.of(0, 0, 0))
        .isBefore(LocalTime.of(23, 59, 59));
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new LocalTimeRandomProvider(MAX_TIME, MIN_TIME))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min local time must be before max local time");
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalTime value = new LocalTimeRandomProvider(null, null)
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalTime.of(22, 6, 36));
  }

  @Test
  void computeValueWithRightRange() {
    final LocalTime value = new LocalTimeRandomProvider(MIN_TIME, MAX_TIME)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value)
        .isAfterOrEqualTo(LocalTime.parse(MIN_TIME))
        .isBefore(LocalTime.parse(MAX_TIME));
  }

  @Test
  void corruptedValueIsIdempotent() {
    final LocalTime value = new LocalTimeRandomProvider(MIN_TIME, MAX_TIME)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalTime.of(16, 52, 42, 408242796));
  }
}