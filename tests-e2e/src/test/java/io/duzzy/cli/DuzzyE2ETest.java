package io.duzzy.cli;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;


public class DuzzyE2ETest {

    @Test
    void shouldPrintHelp() {
        final String help = """
                Usage: duzzy [-hV] [-c=File] [-f=File] [-o=OutputFormat] [-p=Class] [-r=Long]
                             [-s=Long]
                  -c, --config-file=File   Config file used to enrich the schema
                  -f, --schema-file=File   Schema source file
                  -h, --help               Show this help message and exit.
                  -o, --output=OutputFormat
                                           Output format, supported values: RAW, TXT, JSON,
                                             XML, YAML
                  -p, --schema-parser=Class
                                           Qualified name of the parser class used to parse
                                             schema file
                  -r, --rows=Long          Number of rows to generate
                  -s, --seed=Long          Seed used to generate
                  -V, --version            Print version information and exit.
                """;

        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final int exitCode = cmd.execute("-h");
        assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo(help);
    }

    @Test
    void shouldPrintVersion() {
        final String version = "1.0\n";

        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final int exitCode = cmd.execute("-V");
        assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo(version);
    }

    @Test
    void shouldGenerateFromAvroToXMLWithConfig() throws IOException {
        final File expectedFile = getFromResources(getClass(), "result/avro-with-config-result.xml");
        final String expected = Files.readString(Paths.get(expectedFile.toURI()));

        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final int exitCode = cmd.execute(
                "-f=../tests-e2e/src/test/resources/avro-schema/all-supported-fields.avsc",
                "-p=io.duzzy.plugin.parser.AvroSchemaParser",
                "-c=../tests-e2e/src/test/resources/config/duzzy-config-full-xml.yaml",
                "-r=3",
                "-s=1234",
                "-o=TXT"
        );

        final File resultFile = new File("build/avro-with-config-result.xml");
        final String result = Files.readString(Paths.get(resultFile.toURI()));

        assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo("");
        assertThat(result).isEqualTo(expected);
        assertThat(outputStreamCaptor.toString())
                .startsWith("\n\nDuzzy generated 3 rows in PT0")
                .endsWith("S with seed 1234\n");
    }

    @Test
    void shouldGenerateFromAvroToJSONWithConfig() throws IOException {
        final File expectedFile = getFromResources(getClass(), "result/avro-with-config-result.json");
        final String expected = Files.readString(Paths.get(expectedFile.toURI()));

        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final int exitCode = cmd.execute(
                "-f=../tests-e2e/src/test/resources/avro-schema/all-supported-fields.avsc",
                "-p=io.duzzy.plugin.parser.AvroSchemaParser",
                "-c=../tests-e2e/src/test/resources/config/duzzy-config-full-json.yaml",
                "-r=3",
                "-s=1234",
                "-o=TXT"
        );

        final File resultFile = new File("build/avro-with-config-result.json");
        final String result = Files.readString(Paths.get(resultFile.toURI()));

        assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo("");
        assertThat(result).isEqualTo(expected);
        assertThat(outputStreamCaptor.toString())
                .startsWith("\n\nDuzzy generated 3 rows in PT0")
                .endsWith("S with seed 1234\n");
    }

    @Test
    void shouldGenerateFromAvroToCsvWithConfig() throws IOException {
        final File expectedFile = getFromResources(getClass(), "result/avro-with-config-result.csv");
        final String expected = Files.readString(Paths.get(expectedFile.toURI()));

        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final int exitCode = cmd.execute(
                "-f=../tests-e2e/src/test/resources/avro-schema/all-supported-fields.avsc",
                "-p=io.duzzy.plugin.parser.AvroSchemaParser",
                "-c=../tests-e2e/src/test/resources/config/duzzy-config-full-csv.yaml",
                "-r=3",
                "-s=1234",
                "-o=TXT"
        );

        final File resultFile = new File("build/avro-with-config-result.csv");
        final String result = Files.readString(Paths.get(resultFile.toURI()));

        assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo("");
        assertThat(result).isEqualTo(expected);
        assertThat(outputStreamCaptor.toString())
                .startsWith("\n\nDuzzy generated 3 rows in PT0")
                .endsWith("S with seed 1234\n");
    }

    @Test
    void shouldGenerateFromAvroToAvroWithSchemaWithConfig() throws IOException {
        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final int exitCode = cmd.execute(
                "-f=../tests-e2e/src/test/resources/avro-schema/all-supported-fields.avsc",
                "-p=io.duzzy.plugin.parser.AvroSchemaParser",
                "-c=../tests-e2e/src/test/resources/config/duzzy-config-full-avro-with-schema.yaml",
                "-r=3",
                "-s=1234",
                "-o=TXT"
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
            assertThat(outputStreamCaptor.toString())
                    .startsWith("\n\nDuzzy generated 3 rows in PT0")
                    .endsWith("S with seed 1234\n");
        }
    }

    @Test
    void shouldGenerateFromAvroToAvroWithoutSchemaWithConfig() throws IOException {
        final App app = new App();
        final CommandLine cmd = new CommandLine(app);

        final StringWriter sw = new StringWriter();
        cmd.setOut(new PrintWriter(sw));

        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final int exitCode = cmd.execute(
                "-f=../tests-e2e/src/test/resources/avro-schema/all-supported-fields.avsc",
                "-p=io.duzzy.plugin.parser.AvroSchemaParser",
                "-c=../tests-e2e/src/test/resources/config/duzzy-config-full-avro-without-schema.yaml",
                "-r=3",
                "-s=1234",
                "-o=TXT"
        );

        final File expectedFile = getFromResources(
                getClass(),
                "result/avro-with-schema-with-config-result.avro"
        );
        final File resultFile = new File("build/avro-without-schema-with-config-result.avro");
        final BinaryDecoder decoder = DecoderFactory
                .get()
                .binaryDecoder(new FileInputStream(resultFile), null);
        final Schema schema = new Schema
                .Parser()
                .parse(getFromResources(getClass(), "avro-schema/all-supported-fields.avsc"));
        final DatumReader<GenericData.Record> reader = new GenericDatumReader<>();
        reader.setSchema(schema);
        GenericData.Record read = reader.read(null, decoder);
        assertThat(exitCode).isEqualTo(0);
        assertThat(sw.toString()).isEqualTo("");
        assertThat(read).isNotNull();
//        assertThat(records.next()).isEqualTo(expected.next());
//        assertThat(records.next()).isEqualTo(expected.next());
//        assertThat(records.next()).isEqualTo(expected.next());
        assertThat(outputStreamCaptor.toString())
                .startsWith("\n\nDuzzy generated 3 rows in PT0")
                .endsWith("S with seed 1234\n");
    }
}
