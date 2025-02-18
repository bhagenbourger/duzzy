package io.duzzy.cli.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.tests.Helper;
import java.io.File;
import org.junit.jupiter.api.Test;

public class PluginIdentifierTest {

  @Test
  void isValid() {
    final PluginIdentifier pluginIdentifier = new PluginIdentifier(
        "title",
        "desc",
        "vendor",
        "group",
        "artifact",
        "version",
        null
    );

    assertThat(pluginIdentifier.isValid()).isTrue();
  }

  @Test
  void isInvalid() {
    final PluginIdentifier pluginIdentifier = new PluginIdentifier(
        null,
        "desc",
        "vendor",
        "group",
        "artifact",
        "version",
        null
    );

    assertThat(pluginIdentifier.isValid()).isFalse();
  }

  @Test
  void printUnknownWhenTitleIsNull() {
    final PluginIdentifier pluginIdentifier = new PluginIdentifier(null);

    assertThat(pluginIdentifier.printTitle()).isEqualTo("Unknown");
  }

  @Test
  void computeQualifiedNameFromPathWhenGroupOrArtifactOrVersionIsNotSet() {
    final File dummy = Helper.getFromResources(
        PluginManagerTest.class,
        "plugin/dummy.jar"
    );
    final PluginIdentifier pluginIdentifier = new PluginIdentifier(dummy.toPath());

    assertThat(pluginIdentifier.qualifiedName()).isEqualTo("dummy");
  }

  @Test
  void computeUnversionedQualifiedNameFromPathWhenGroupOrArtifactOrVersionIsNotSet() {
    final File dummy = Helper.getFromResources(
        PluginManagerTest.class,
        "plugin/dummy.jar"
    );
    final PluginIdentifier pluginIdentifier = new PluginIdentifier(dummy.toPath());

    assertThat(pluginIdentifier.unversionedQualifiedName()).isEqualTo("dummy");
  }

  @Test
  void computeUnversionedQualifiedName() {
    final PluginIdentifier pluginIdentifier = new PluginIdentifier(
        "title",
        "desc",
        "vendor",
        "group",
        "artifact",
        "version",
        null
    );

    assertThat(pluginIdentifier.unversionedQualifiedName()).isEqualTo("group_artifact");
  }
}
