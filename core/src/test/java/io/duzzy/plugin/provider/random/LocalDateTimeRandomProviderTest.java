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
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LocalDateTimeRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/local-date-time-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateTimeRandomProvider.class);
    assertThat((LocalDateTime) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(LocalDateTime.of(2020, 5, 26, 10, 0, 0))
        .isBefore(LocalDateTime.of(2020, 5, 27, 10, 0, 1));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/local-date-time-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateTimeRandomProvider.class);
    assertThat((LocalDateTime) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(LocalDateTime.MIN)
        .isBeforeOrEqualTo(LocalDateTime.MAX);
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(
        () -> new LocalDateTimeRandomProvider("2020-10-10T10:00:00", "2000-01-01T10:00:00"))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min local date time must be before max local date time");
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalDateTime value = new LocalDateTimeRandomProvider(null, null)
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDateTime.of(2004, 6, 14, 22, 6, 36));
  }

  @Test
  void computeValueWithRightRange() {
    final LocalDateTime value =
        new LocalDateTimeRandomProvider("2020-05-30T10:00:00", "2021-05-30T10:00:00")
            .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value)
        .isAfterOrEqualTo(LocalDateTime.parse("2020-05-30T10:00:00"))
        .isBeforeOrEqualTo(LocalDateTime.parse("2021-05-30T10:00:00"));
  }

  @Test
  void corruptedValueIsIdempotent() {
    final LocalDateTime value =
        new LocalDateTimeRandomProvider("2020-05-30T10:00:00", "2021-05-30T10:00:00")
            .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDateTime.of(618906090, 7, 17, 22, 8, 22, 880641847));
  }
}
