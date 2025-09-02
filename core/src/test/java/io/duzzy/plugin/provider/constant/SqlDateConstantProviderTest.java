package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class SqlDateConstantProviderTest {

  private static final String VAL = "2023-01-01";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/sqldate-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlDateConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(Date.from(Instant.parse("2023-01-01T00:00:00Z")));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/sqldate-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlDateConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Date value = new SqlDateConstantProvider(Date.valueOf(VAL))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Date.valueOf(VAL));
  }

  @Test
  void computeValueIsConstant() {
    final Date value = new SqlDateConstantProvider(Date.valueOf(VAL))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Date.valueOf(VAL));
  }
}
