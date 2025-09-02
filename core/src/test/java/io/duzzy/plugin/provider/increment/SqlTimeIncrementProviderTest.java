package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import org.junit.jupiter.api.Test;

public class SqlTimeIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/sql-time-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlTimeIncrementProvider.class);
    assertThat((Time) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(Time.valueOf("01:00:00"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/sql-time-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlTimeIncrementProvider.class);
    assertThat((Time) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Time value = new SqlTimeIncrementProvider("00:00:00", 1L, "SECONDS")
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Time.valueOf("00:00:01"));
  }

  @Test
  void computeValueWithRightRange() {
    final Time value = new SqlTimeIncrementProvider("00:00:00", 1L, "HOURS")
        .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(Time.valueOf("05:00:00"));
  }
}
