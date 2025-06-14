package io.duzzy.cli;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

public class AppNativeTest {
  private static final String HELP = """
      Usage: duzzy [-hV] [COMMAND]
      Give me your schema, I'll give you your test data.
        -h, --help      Show this help message and exit.
        -V, --version   Print version information and exit.
      Commands:
        run  Generate your test data
        doc  Print Duzzy documentation
      """;

  @Test
  void shouldPrintHelp() {
    final AppNative app = new AppNative();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final int exitCode = cmd.execute("-h");
    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo(HELP);
  }

  @Test
  void shouldPrintVersion() {
    final String version = "0.0.0\n";

    final AppNative app = new AppNative();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final int exitCode = cmd.execute("-V");
    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo(version);
  }

  @Test
  void shouldPrintUsageWhenArgsIsEmpty() {
    final AppNative app = new AppNative();
    final CommandLine cmd = new CommandLine(app);

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute();
    assertThat(exitCode).isEqualTo(0);
    assertThat(outputStreamCaptor.toString(UTF_8)).isEqualTo(HELP);
  }
}
