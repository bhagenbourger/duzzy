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
                {"city":"ixO7KVnF2g9v"}
                {"city":"fzJL0NaCYFQ"}
                {"city":"KTMudD284TdR"}
                {"city":"1NqREw1ypI"}
                {"city":"12sGlywJG43"}
                {"city":"367YDIVwXtX"}
                {"city":"aE1lt67oJ6Neu"}
                {"city":"wIsUgerpfGgW"}
                {"city":"WrBw98FAPe1AiT"}
                {"city":"uLri7KqkjVTcL0"}""";
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
        final DuzzyResult duzzyResult = new Duzzy(duzzySchemaFile, null, 1L, null, null).generate();

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
                "<country><city>ixO7KVnF2g9v</city></country>" +
                "<country><city>fzJL0NaCYFQ</city></country>" +
                "<country><city>KTMudD284TdR</city></country>" +
                "<country><city>1NqREw1ypI</city></country>" +
                "<country><city>12sGlywJG43</city></country>" +
                "<country><city>367YDIVwXtX</city></country>" +
                "<country><city>aE1lt67oJ6Neu</city></country>" +
                "<country><city>wIsUgerpfGgW</city></country>" +
                "<country><city>WrBw98FAPe1AiT</city></country>" +
                "<country><city>uLri7KqkjVTcL0</city></country>" +
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
