package io.duzzy.cli.command;

import static io.duzzy.tests.Helper.deleteDirectory;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.tests.Helper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PluginCommandTest {

  private static final String HOME = "build/tmp/test/home";
  private static final Path PLUGIN_HOME = Paths.get(HOME, ".duzzy", "plugins");
  private static final PluginCommand PLUGIN_COMMAND = new PluginCommand(HOME);

  private static ByteArrayOutputStream setConsoleOutput() {
    final ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
    return output;
  }

  @BeforeEach
  void setUp() throws IOException {
    if (!Files.exists(PLUGIN_HOME)) {
      Files.createDirectories(PLUGIN_HOME);
    }
  }

  @AfterEach
  void cleanup() throws IOException {
    deleteDirectory(Paths.get(HOME));
  }

  @Test
  void installPlugin() {
    final ByteArrayOutputStream output = setConsoleOutput();
    final File jarFile = Helper.getFromResources(
        PluginCommandTest.class,
        "plugin/duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
    );
    final int result = PLUGIN_COMMAND.install(jarFile.getAbsolutePath());
    assertThat(result).isEqualTo(0);
    assertThat(output.toString(StandardCharsets.UTF_8))
        .isEqualTo("Plugin Duzzy plugin template is installed.\n");
  }

  @Test
  void uninstallPluginThatNotExists() {
    final ByteArrayOutputStream output = setConsoleOutput();

    final int result = PLUGIN_COMMAND.uninstall("toto");

    assertThat(result).isEqualTo(0);
    assertThat(output.toString(StandardCharsets.UTF_8))
        .isEqualTo("Plugin toto is not installed.\n");
  }

  @Test
  void uninstallPluginThatExists() throws IOException {
    final ByteArrayOutputStream output = setConsoleOutput();

    final String qualifiedName = "io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT";
    final File jarFile = Helper.getFromResources(
        PluginCommandTest.class,
        "plugin/duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
    );
    Files.copy(
        jarFile.toPath(),
        Paths.get(PLUGIN_HOME.toAbsolutePath().toString(), qualifiedName + ".jar")
    );

    final int result = PLUGIN_COMMAND.uninstall(qualifiedName);

    final String expected = "Plugin io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT "
        + "successfully deleted.\n";
    assertThat(result).isEqualTo(0);
    assertThat(output.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }

  @Test
  void list() throws IOException {
    final ByteArrayOutputStream output = setConsoleOutput();

    final File validPlugin = Helper.getFromResources(
        PluginCommandTest.class,
        "plugin/duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
    );
    final File dummyPlugin = Helper.getFromResources(
        PluginCommandTest.class,
        "plugin/dummy.jar"
    );

    Files.copy(
        validPlugin.toPath(),
        Paths.get(
            PLUGIN_HOME.toAbsolutePath().toString(),
            "duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar"
        )
    );
    Files.copy(
        dummyPlugin.toPath(),
        Paths.get(
            PLUGIN_HOME.toAbsolutePath().toString(),
            "dummy.jar"
        )
    );

    final int result = PLUGIN_COMMAND.list();

    final String expected = """
        Installed plugins:
        - Duzzy plugin template
        \tqualified name: io.duzzy.external.plugin_duzzy-plugin-template_0.0.0-SNAPSHOT
        \tdescription: A template to start to create your own plugin for duzzy
        \tvendor: Duzzy
        \tgroupId: io.duzzy.external.plugin
        \tartifactId: duzzy-plugin-template
        \tversion: 0.0.0-SNAPSHOT
        \tjar:""" + " " + PLUGIN_HOME.toAbsolutePath() + """
        /duzzy-plugin-template-0.0.0-SNAPSHOT-all.jar
        - Unknown
        \tqualified name: dummy
        \tdescription: Unknown
        \tvendor: Unknown
        \tgroupId: Unknown
        \tartifactId: Unknown
        \tversion: Unknown
        \tjar:""" + " " + PLUGIN_HOME.toAbsolutePath() + """
        /dummy.jar
        """;
    assertThat(result).isEqualTo(0);
    assertThat(output.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }
}
