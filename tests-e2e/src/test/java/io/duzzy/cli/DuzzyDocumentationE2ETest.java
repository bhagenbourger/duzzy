package io.duzzy.cli;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class DuzzyDocumentationE2ETest {

    @Test
    void generateDoc() {
        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final int exitCode = cmd.execute("doc");

        Assertions.assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo("");
    }
}
