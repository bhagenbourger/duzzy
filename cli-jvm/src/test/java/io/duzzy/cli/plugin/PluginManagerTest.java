package io.duzzy.cli.plugin;

import static io.duzzy.tests.Helper.deleteDirectory;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.tests.Helper;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class PluginManagerTest {

  private static final String HOME = "build/tmp/test/home";

  private static PluginManager installTemplatePlugin() throws IOException, URISyntaxException {
    final File jarFile = Helper.getFromResources(
        PluginManagerTest.class,
        "plugin/duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
    );

    final PluginManager pluginManager = new PluginManager(HOME);
    pluginManager.install(jarFile.getAbsolutePath());
    return pluginManager;
  }

  @AfterEach
  void cleanup() throws IOException {
    deleteDirectory(Paths.get(HOME));
  }

  @Test
  void installAndListPluginFromLocal() throws IOException, URISyntaxException {
    final PluginManager pluginManager = installTemplatePlugin();

    final List<PluginIdentifier> plugins = pluginManager.list();
    final PluginIdentifier expected = new PluginIdentifier(
        "Duzzy plugin template",
        "A template to start to create your own plugin for duzzy",
        "Duzzy",
        "io.duzzy.external.plugin",
        "duzzy-plugin-template",
        "0.0.0-SNAPSHOT",
        Paths.get(
            pluginManager.getDuzzyPluginsHome().toString(),
            "io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT.jar"
        )
    );
    assertThat(plugins).contains(expected);
  }

  @Test
  void listInstalledPlugin() throws IOException, URISyntaxException {
    final PluginManager pluginManager = installTemplatePlugin();
    final String output = pluginManager.list().getFirst().print();
    final String expectedStart =
        """
            - Duzzy plugin template
            \tqualified name: io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT
            \tdescription: A template to start to create your own plugin for duzzy
            \tvendor: Duzzy
            \tgroupId: io.duzzy.external.plugin
            \tartifactId: duzzy-plugin-template
            \tversion: 0.0.0-SNAPSHOT
            \tjar:\s""";
    final String expectedEnd = "/build/tmp/test/home/.duzzy/plugins/"
        + "io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT.jar";

    assertThat(output).startsWith(expectedStart);
    assertThat(output).endsWith(expectedEnd);
  }

  @Test
  void uninstallPlugin() throws IOException, URISyntaxException {
    final PluginManager pluginManager = installTemplatePlugin();
    pluginManager.uninstall("io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT");

    final List<PluginIdentifier> plugins = pluginManager.list();
    assertThat(plugins).isEmpty();
  }

  @Test
  void checkIfAnotherVersionAlreadyExists() throws IOException, URISyntaxException {
    final File jarFile = Helper.getFromResources(
        PluginManagerTest.class,
        "plugin/duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
    );
    final String expected =
        "Plugin Duzzy plugin template is already installed in versions 0.0.0-SNAPSHOT.\n"
            + "You must uninstall this version before: "
            + "duzzy plugin uninstall "
            + "io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT";

    final PluginManager pluginManager = new PluginManager(HOME);
    pluginManager.install(jarFile.getAbsolutePath());
    final String result = pluginManager.install(jarFile.getAbsolutePath());
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void checkIfAnotherVersionsAlreadyExists() throws IOException, URISyntaxException {
    final File version000 = Helper.getFromResources(
        PluginManagerTest.class,
        "plugin/duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
    );
    final File version010 = Helper.getFromResources(
        PluginManagerTest.class,
        "plugin/duzzy-plugin-template-0.1.0-SNAPSHOT-all.jar"
    );
    final String expected =
        """
            Plugin Duzzy plugin template is already installed in versions \
            0.0.0-SNAPSHOT, 0.1.0-SNAPSHOT.
            You must uninstall this version before: \
            duzzy plugin uninstall io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT
            You must uninstall this version before: \
            duzzy plugin uninstall io.duzzy.external.plugin_duzzy-plugin-template_0.1.0-SNAPSHOT""";

    final PluginManager pluginManager = new PluginManager(HOME);
    final Path duzzyPluginsHome = pluginManager.getDuzzyPluginsHome();
    Files.createDirectories(duzzyPluginsHome);
    final String absoluteHome = duzzyPluginsHome.toAbsolutePath().toString();
    Files.copy(version000.toPath(), Path.of(absoluteHome, version000.getName()));
    Files.copy(version010.toPath(), Path.of(absoluteHome, version010.getName()));
    final String result = pluginManager.install(version010.getAbsolutePath());
    assertThat(result).isEqualTo(expected);
  }
}
