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
import java.sql.Time;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

public class SqlTimeRandomProviderTest {

  private static final String MIN_TIME = "10:00:00";
  private static final String MAX_TIME = "11:00:00";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/sql-time-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlTimeRandomProvider.class);
    assertThat((Time) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(Time.valueOf(LocalTime.parse(MIN_TIME)))
        .isBefore(Time.valueOf(LocalTime.parse(MAX_TIME)));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/sql-time-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlTimeRandomProvider.class);
    assertThat((Time) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(Time.valueOf(LocalTime.of(0, 0, 0)))
        .isBefore(Time.valueOf(LocalTime.of(23, 59, 59)));
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new SqlTimeRandomProvider(MAX_TIME, MIN_TIME))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min time must be before max time");
  }

  @Test
  void computeValueIsIdempotent() {
    final Time value = new SqlTimeRandomProvider(null, null).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Time.valueOf(LocalTime.of(22, 6, 36)));
  }

  @Test
  void computeValueWithRightRange() {
    final Time value = new SqlTimeRandomProvider(MIN_TIME, MAX_TIME)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value)
        .isAfterOrEqualTo(Time.valueOf(LocalTime.parse(MIN_TIME)))
        .isBefore(Time.valueOf(LocalTime.parse(MAX_TIME)));
  }

  @Test
  void corruptedValueIsIdempotent() {
    final SqlTimeRandomProvider provider = new SqlTimeRandomProvider(MIN_TIME, MAX_TIME);
    final Time value1 = provider.corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    final Time value2 = provider.corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value1).isEqualTo(value2);
  }
}
