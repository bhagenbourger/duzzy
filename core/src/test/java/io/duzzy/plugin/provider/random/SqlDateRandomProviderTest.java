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
import java.sql.Date;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class SqlDateRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/sql-date-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlDateRandomProvider.class);
    assertThat((Date) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(Date.valueOf(LocalDate.of(2020, 1, 1)))
        .isBefore(Date.valueOf(LocalDate.of(2021, 1, 1)));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/sql-date-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlDateRandomProvider.class);
    assertThat((Date) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(Date.valueOf(LocalDate.of(1970, 1, 1)))
        .isBefore(Date.valueOf(LocalDate.of(9999, 12, 31)));
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new SqlDateRandomProvider("2020-10-10", "2000-01-01"))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min date must be before max date");
  }

  @Test
  void computeValueIsIdempotent() {
    final Date value = new SqlDateRandomProvider(null, null).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Date.valueOf(LocalDate.of(2016, 1, 30)));
  }

  @Test
  void computeValueWithRightRange() {
    final Date value = new SqlDateRandomProvider("2020-05-30", "2021-05-30")
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value)
        .isAfterOrEqualTo(Date.valueOf(LocalDate.parse("2020-05-30")))
        .isBefore(Date.valueOf(LocalDate.parse("2021-05-30")));
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Date value = new SqlDateRandomProvider("2020-05-30", "2021-05-30")
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value.toLocalDate()).isEqualTo(LocalDate.of(157311039, 12, 28));
  }
}
