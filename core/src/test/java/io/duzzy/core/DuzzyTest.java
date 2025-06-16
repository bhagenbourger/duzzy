package io.duzzy.core;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class DuzzyTest {

  @Test
  void generateJsonInConsoleFromDuzzySchema() throws Exception {
    final String expected = """
        {"city":"xO7KVnF2g9"}
        {"city":"zJL0NaCYFQq8D"}
        {"city":"KTMudD284TdRIw"}
        {"city":"1NqREw1ypIG"}
        {"city":"2sGlywJG43F"}
        {"city":"67YDIVwXtX"}
        {"city":"E1lt67oJ6Ne"}
        {"city":"IsUgerpfGgWRl3H"}
        {"city":"rBw98FAPe1"}
        {"city":"Lri7KqkjVTcL0"}\n""";
    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));

    final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema.yaml");

    final DuzzyResult duzzyResult = new Duzzy(
        duzzySchemaFile,
        null,
        1L,
        null,
        null,
        null
    ).generate();

    assertThat(duzzyResult.rows()).isEqualTo(10L);
    assertThat(duzzyResult.seed()).isEqualTo(1L);
    assertThat(outputStreamCaptor.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }

  @Test
  void generateJsonInConsoleFromDuzzySchemaWithAllFields() throws Exception {
    final File expectedFile =
        getFromResources(getClass(), "result/expected-duzzy-schema-all-fields.jsonl");
    final String expected = Files.readString(Paths.get(expectedFile.toURI()));

    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));

    final File duzzySchemaFile =
        getFromResources(getClass(), "schema/duzzy-schema-all-fields.yaml");
    final DuzzyResult duzzyResult = new Duzzy(
        duzzySchemaFile,
        null,
        1L,
        42L,
        null,
        null
    ).generate();

    assertThat(duzzyResult.rows()).isEqualTo(42L);
    assertThat(duzzyResult.seed()).isEqualTo(1L);
    assertThat(outputStreamCaptor.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }

  @Test
  void generateXmlInConsoleFromDuzzySchema() throws Exception {
    final String expected = "<?xml version='1.0' encoding='UTF-8'?>"
        + "<countries>"
        + "<country><city>xO7KVnF2g9</city></country>"
        + "<country><city>zJL0NaCYFQq8D</city></country>"
        + "<country><city>KTMudD284TdRIw</city></country>"
        + "<country><city>1NqREw1ypIG</city></country>"
        + "<country><city>2sGlywJG43F</city></country>"
        + "<country><city>67YDIVwXtX</city></country>"
        + "<country><city>E1lt67oJ6Ne</city></country>"
        + "<country><city>IsUgerpfGgWRl3H</city></country>"
        + "<country><city>rBw98FAPe1</city></country>"
        + "<country><city>Lri7KqkjVTcL0</city></country>"
        + "</countries>\n";
    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));

    final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-xml.yaml");
    final File configFile = getFromResources(getClass(), "config/duzzy-config-xml.yaml");

    final DuzzyResult duzzyResult = new Duzzy(
        duzzySchemaFile,
        configFile,
        1L,
        null,
        null,
        null
    ).generate();

    assertThat(duzzyResult.rows()).isEqualTo(10L);
    assertThat(duzzyResult.seed()).isEqualTo(1L);
    assertThat(outputStreamCaptor.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }
}
