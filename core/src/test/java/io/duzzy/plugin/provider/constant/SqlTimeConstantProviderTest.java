package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import org.junit.jupiter.api.Test;

public class SqlTimeConstantProviderTest {

  private static final String VAL = "12:30:00";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/sqltime-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlTimeConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(Time.valueOf("12:30:00"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/sqltime-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlTimeConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Time value = new SqlTimeConstantProvider(Time.valueOf(VAL))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Time.valueOf(VAL));
  }

  @Test
  void computeValueIsConstant() {
    final Time value = new SqlTimeConstantProvider(Time.valueOf(VAL))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Time.valueOf(VAL));
  }
}
