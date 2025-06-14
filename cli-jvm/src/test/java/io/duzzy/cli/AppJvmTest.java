package io.duzzy.cli;

import static io.duzzy.tests.Helper.getFromResources;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import picocli.CommandLine;

public class AppJvmTest {
  private static final String HELP = """
      Usage: duzzy [-hV] [COMMAND]
      Give me your schema, I'll give you your test data.
        -h, --help      Show this help message and exit.
        -V, --version   Print version information and exit.
      Commands:
        run     Generate your test data
        plugin  Manage your plugins
        doc     Print Duzzy documentation
      """;
  private static final String RUN_ACTION = "run";
  private static final String SCHEMA_FILE_OPTION =
      "-f=../cli-jvm/src/test/resources/avro-schema/all-supported-fields.avsc";
  private static final String PARSER_OPTION = "-p=io.duzzy.plugin.parser.AvroSchemaParser";
  private static final String ROWS_OPTION = "-r=3";
  private static final String SEAD_OPTION = "-s=1234";
  private static final String OUTPUT_OPTION = "-o=TXT";
  private static final String RESULT_STARTS_WITH = "Duzzy generated 3 rows in PT0";
  private static final String RESULT_ENDS_WITH = "bytes of data with seed 1234\n";

  @Test
  void shouldPrintHelp() {
    final AppJvm app = new AppJvm();
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

    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final int exitCode = cmd.execute("-V");
    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo(version);
  }

  @Test
  void shouldPrintUsageWhenArgsIsEmpty() {
    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute();
    assertThat(exitCode).isEqualTo(0);
    assertThat(outputStreamCaptor.toString(UTF_8)).isEqualTo(HELP);
  }

  @Test
  void shouldGenerateFromAvroToXmlWithConfig() throws IOException {
    final File expectedFile = getFromResources(getClass(), "result/avro-with-config-result.xml");
    final String expected = Files.readString(Paths.get(expectedFile.toURI()));

    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute(
        RUN_ACTION,
        SCHEMA_FILE_OPTION,
        PARSER_OPTION,
        "-c=../cli-jvm/src/test/resources/config/duzzy-config-full-xml.yaml",
        ROWS_OPTION,
        SEAD_OPTION,
        OUTPUT_OPTION
    );

    final File resultFile = new File("build/avro-with-config-result.xml");
    final String result = Files.readString(Paths.get(resultFile.toURI()));

    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo("");
    assertThat(result).isEqualTo(expected);
    assertThat(outputStreamCaptor.toString(UTF_8))
        .startsWith(RESULT_STARTS_WITH)
        .endsWith(RESULT_ENDS_WITH);
  }

  @Test
  void shouldGenerateFromAvroToJsonWithConfig() throws IOException {
    final File expectedFile = getFromResources(getClass(), "result/avro-with-config-result.json");
    final String expected = Files.readString(Paths.get(expectedFile.toURI()));

    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute(
        RUN_ACTION,
        SCHEMA_FILE_OPTION,
        PARSER_OPTION,
        "-c=../cli-jvm/src/test/resources/config/duzzy-config-full-json.yaml",
        ROWS_OPTION,
        SEAD_OPTION,
        OUTPUT_OPTION
    );

    final File resultFile = new File("build/avro-with-config-result.json");
    final String result = Files.readString(Paths.get(resultFile.toURI()));

    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo("");
    assertThat(result).isEqualTo(expected);
    assertThat(outputStreamCaptor.toString(UTF_8))
        .startsWith(RESULT_STARTS_WITH)
        .endsWith(RESULT_ENDS_WITH);
  }

  @Test
  void shouldGenerateFromAvroToCsvWithConfig() throws IOException {
    final File expectedFile = getFromResources(getClass(), "result/avro-with-config-result.csv");
    final String expected = Files.readString(Paths.get(expectedFile.toURI()));

    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute(
        RUN_ACTION,
        SCHEMA_FILE_OPTION,
        PARSER_OPTION,
        "-c=../cli-jvm/src/test/resources/config/duzzy-config-full-csv.yaml",
        ROWS_OPTION,
        SEAD_OPTION,
        OUTPUT_OPTION
    );

    final File resultFile = new File("build/avro-with-config-result.csv");
    final String result = Files.readString(Paths.get(resultFile.toURI()));

    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo("");
    assertThat(result).isEqualTo(expected);
    assertThat(outputStreamCaptor.toString(UTF_8))
        .startsWith(RESULT_STARTS_WITH)
        .endsWith(RESULT_ENDS_WITH);
  }

  @Test
  void shouldGenerateFromAvroToAvroWithSchemaWithConfig() throws IOException {
    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute(
        RUN_ACTION,
        SCHEMA_FILE_OPTION,
        PARSER_OPTION,
        "-c=../cli-jvm/src/test/resources/config/duzzy-config-full-avro-with-schema.yaml",
        ROWS_OPTION,
        SEAD_OPTION,
        OUTPUT_OPTION
    );

    final File expectedFile = getFromResources(
        getClass(),
        "result/avro-with-schema-with-config-result.avro"
    );
    final File resultFile = new File("build/avro-with-schema-with-config-result.avro");
    try (
        final DataFileReader<GenericData.Record> records =
            new DataFileReader<>(resultFile, new GenericDatumReader<>());
        final DataFileReader<GenericData.Record> expected =
            new DataFileReader<>(expectedFile, new GenericDatumReader<>())
    ) {
      assertThat(exitCode).isEqualTo(0);
      assertThat(sw.toString()).isEqualTo("");
      assertThat(records.next().compareTo(expected.next())).isEqualTo(0);
      assertThat(records.next().compareTo(expected.next())).isEqualTo(0);
      assertThat(records.next().compareTo(expected.next())).isEqualTo(0);
      assertThat(outputStreamCaptor.toString(UTF_8))
          .startsWith(RESULT_STARTS_WITH)
          .endsWith(RESULT_ENDS_WITH);
    }
  }

  @Test
  void shouldGenerateFromAvroToAvroSchemalessWithConfig() throws IOException {
    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute(
        RUN_ACTION,
        SCHEMA_FILE_OPTION,
        PARSER_OPTION,
        "-c=../cli-jvm/src/test/resources/config/duzzy-config-full-avro-schemaless.yaml",
        ROWS_OPTION,
        SEAD_OPTION,
        OUTPUT_OPTION
    );

    final Schema schema = new Schema
        .Parser()
        .parse(getFromResources(getClass(), "avro-schema/all-supported-fields.avsc"));
    final DatumReader<GenericData.Record> reader = new GenericDatumReader<>();
    reader.setSchema(schema);
    final File resultFile = new File("build/avro-schemaless-with-config-result.avro");

    try (final InputStream fileInputStream = new FileInputStream(resultFile)) {
      final BinaryDecoder decoder = DecoderFactory
          .get()
          .binaryDecoder(fileInputStream, null);
      final GenericData.Record read = reader.read(null, decoder);
      assertThat(read).isNotNull();
    }

    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo("");
    assertThat(outputStreamCaptor.toString(UTF_8))
        .startsWith(RESULT_STARTS_WITH)
        .endsWith(RESULT_ENDS_WITH);
  }

  @Test
  void shouldPrintPluginUsage() {
    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute("plugin");
    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo("");
    assertThat(outputStreamCaptor.toString(UTF_8)).isEqualTo("""
        Usage: plugin [-hV] [COMMAND]
        Manage your plugins
          -h, --help      Show this help message and exit.
          -V, --version   Print version information and exit.
        Commands:
          install    Install a plugin
          list       List all installed plugins
          uninstall  Uninstall a plugin
        """);
  }

  @Test
  void installPluginRequiredSourceOption() {
    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setErr(new PrintWriter(sw));

    final int exitCode = cmd.execute("plugin", "install");
    assertThat(exitCode).isEqualTo(2);
    assertThat(sw.toString()).isEqualTo("""
        Missing required option: '--source=String'
        Usage: duzzy plugin install [-hV] -s=String
        Install a plugin
          -h, --help            Show this help message and exit.
          -s, --source=String   Url or local path to the plugin
          -V, --version         Print version information and exit.
        """);
  }

  @ParameterizedTest
  @CsvSource({
      "doc, doc/doc.txt",
      "doc -i= -m= -n=, doc/doc.txt",
      "doc -t=PARSER, doc/doc-parser.txt",
      "doc -i=sql, doc/doc-sql.txt",
      "doc -n=true, doc/doc-native.txt",
      "doc -m=plugin-avro, doc/doc-avro.txt",
      "doc -t=PARSER -i=sql -m=core, doc/doc-parser-sql.txt"
  })
  void generateDoc(String command, String result) throws IOException {
    final AppJvm app = new AppJvm();
    final CommandLine cmd = new CommandLine(app);

    final StringWriter sw = new StringWriter();
    cmd.setOut(new PrintWriter(sw));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, UTF_8));

    final int exitCode = cmd.execute(command.split(" "));

    assertThat(exitCode).isEqualTo(0);
    assertThat(sw.toString()).isEqualTo("");

    final File file = getFromResources(getClass(), result);
    try (final FileInputStream input = new FileInputStream(file)) {
      final String doc = new String(input.readAllBytes(), UTF_8);
      assertThat(outputStreamCaptor.toString(UTF_8)).isEqualTo(doc);
    }
  }
}
