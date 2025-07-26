package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import org.junit.jupiter.api.Test;

public class SqlDateIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/sql-date-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlDateIncrementProvider.class);
    assertThat((Date) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(Date.valueOf("2023-01-02"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/sql-date-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(SqlDateIncrementProvider.class);
    assertThat((Date) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Date value = new SqlDateIncrementProvider("2023-01-01", 1L, "DAYS")
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(Date.valueOf("2023-01-02"));
  }

  @Test
  void computeValueWithRightRange() {
    final Date value = new SqlDateIncrementProvider("2023-01-01", 1L, "DAYS")
        .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(Date.valueOf("2023-01-06"));
  }
}
