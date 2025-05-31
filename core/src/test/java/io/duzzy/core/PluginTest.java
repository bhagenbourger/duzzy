package io.duzzy.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class PluginTest {

  private static final String PLUGIN_IDENTIFIER = "io.duzzy.core.MockPlugin";

  @Test
  void shouldHaveDefaultIdentifier() {
    final String identifier = new MockPlugin().getIdentifier();

    Assertions.assertThat(identifier).isEqualTo(PLUGIN_IDENTIFIER);
  }
}
