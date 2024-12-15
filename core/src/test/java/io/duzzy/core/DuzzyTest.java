package io.duzzy.core;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class DuzzyTest {

    @Test
    void generateJsonInConsoleFromDuzzySchema()
            throws IOException,
            ClassNotFoundException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
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
                {"city":"Lri7KqkjVTcL0"}""";
        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema.yaml");

        final DuzzyResult duzzyResult = new Duzzy(duzzySchemaFile, null, 1L, null, null).generate();

        assertThat(duzzyResult.rows()).isEqualTo(10L);
        assertThat(duzzyResult.seed()).isEqualTo(1L);
        assertThat(outputStreamCaptor.toString()).isEqualTo(expected);
    }

    @Test
    void generateJsonInConsoleFromDuzzySchemaWithAllColumns()
            throws IOException,
            ClassNotFoundException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        final File expectedFile = getFromResources(getClass(), "result/expected-duzzy-schema-all-coumns.jsonl");
        final String expected = Files.readString(Paths.get(expectedFile.toURI()));

        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-all-columns.yaml");
        final DuzzyResult duzzyResult = new Duzzy(duzzySchemaFile, null, 1L, 42L, null).generate();

        assertThat(duzzyResult.rows()).isEqualTo(42L);
        assertThat(duzzyResult.seed()).isEqualTo(1L);
        assertThat(outputStreamCaptor.toString()).isEqualTo(expected);
    }

    @Test
    void generateXmlInConsoleFromDuzzySchema()
            throws IOException,
            ClassNotFoundException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException {
        final String expected = "<?xml version='1.0' encoding='UTF-8'?>" +
                "<countries>" +
                "<country><city>xO7KVnF2g9</city></country>" +
                "<country><city>zJL0NaCYFQq8D</city></country>" +
                "<country><city>KTMudD284TdRIw</city></country>" +
                "<country><city>1NqREw1ypIG</city></country>" +
                "<country><city>2sGlywJG43F</city></country>" +
                "<country><city>67YDIVwXtX</city></country>" +
                "<country><city>E1lt67oJ6Ne</city></country>" +
                "<country><city>IsUgerpfGgWRl3H</city></country>" +
                "<country><city>rBw98FAPe1</city></country>" +
                "<country><city>Lri7KqkjVTcL0</city></country>" +
                "</countries>";
        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final File duzzySchemaFile = getFromResources(getClass(), "schema/duzzy-schema-xml.yaml");

        final DuzzyResult duzzyResult = new Duzzy(duzzySchemaFile, null, 1L, null, null).generate();

        assertThat(duzzyResult.rows()).isEqualTo(10L);
        assertThat(duzzyResult.seed()).isEqualTo(1L);
        assertThat(outputStreamCaptor.toString()).isEqualTo(expected);
    }
}
