package io.duzzy.core.config;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.sink.LocalFileSink;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class DuzzyConfigTest {

  @Test
  void shouldParsedFromYamlFile() throws IOException {
    final File duzzyConfigFile = getFromResources(getClass(), "config/duzzy-config-full.yaml");
    final DuzzyConfig duzzyConfig = DuzzyConfig.fromFile(duzzyConfigFile);

    assertThat(duzzyConfig.columns()).hasSize(1);
    assertThat(duzzyConfig.columns().getFirst().querySelector())
        .isEqualTo("name=city");
    assertThat(duzzyConfig.columns().getFirst().identifier())
        .isEqualTo("io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
    assertThat(duzzyConfig.columns().getFirst().parameters())
        .isEqualTo(Map.of("min_length", 3, "max_length", 20));
    assertThat(duzzyConfig.sink()).isInstanceOf(LocalFileSink.class);
  }

  @Test
  void shouldFindColumn() throws IOException {
    final File duzzyConfigFile = getFromResources(getClass(), "config/duzzy-config-full.yaml");
    final DuzzyConfig duzzyConfig = DuzzyConfig.fromFile(duzzyConfigFile);

    assertThat(duzzyConfig.findProvider("name", "city").get())
        .isInstanceOf(AlphanumericRandomProvider.class);
  }
}
