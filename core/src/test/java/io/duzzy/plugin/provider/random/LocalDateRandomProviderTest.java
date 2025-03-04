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
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class LocalDateRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/local-date-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateRandomProvider.class);
    assertThat((LocalDate) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(LocalDate.of(2020, 5, 26))
        .isBefore(LocalDate.of(2020, 5, 27));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/local-date-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateRandomProvider.class);
    assertThat((LocalDate) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(LocalDate.MIN)
        .isBeforeOrEqualTo(LocalDate.MAX);
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new LocalDateRandomProvider("2020-10-10", "2000-01-01"))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min local date must be before max local date");
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalDate value = new LocalDateRandomProvider(null, null)
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("2014-04-07");
  }

  @Test
  void computeValueWithRightRange() {
    final LocalDate value = new LocalDateRandomProvider("2020-05-30", "2021-05-30")
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value)
        .isAfterOrEqualTo("2020-05-30")
        .isBeforeOrEqualTo("2021-05-30");
  }

  @Test
  void corruptedValueIsIdempotent() {
    final LocalDate value = new LocalDateRandomProvider("2020-05-30", "2021-05-30")
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("+486231866-06-06");
  }
}
