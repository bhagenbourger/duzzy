package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class LocalDateIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/local-date-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateIncrementProvider.class);
    assertThat((LocalDate) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(LocalDate.parse("2023-01-02"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/local-date-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateIncrementProvider.class);
    assertThat((LocalDate) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalDate value = new LocalDateIncrementProvider("2023-01-01", 1L, "DAYS")
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDate.parse("2023-01-02"));
  }

  @Test
  void computeValueWithRightRange() {
    final LocalDate value = new LocalDateIncrementProvider("2023-01-01", 1L, "DAYS")
        .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(LocalDate.parse("2023-01-06"));
  }

  @Test
  void corruptedValueIsIdempotent() {
    final LocalDate value = new LocalDateIncrementProvider("2023-01-01", 1L, "DAYS")
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDate.of(486231866, 6, 6));
  }
}
