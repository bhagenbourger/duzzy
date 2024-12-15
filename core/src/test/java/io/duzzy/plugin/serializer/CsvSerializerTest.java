package io.duzzy.plugin.serializer;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.serializer.Serializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class CsvSerializerTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File serializerFile = getFromResources(getClass(), "serializer/csv-serializer.yaml");
        final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

        assertThat(serializer).isInstanceOf(CsvSerializer.class);
    }

    @Test
    void writeCsvWithDefaultValues() throws IOException {
        final String expected = "1,one\n2,two\n";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final CsvSerializer csvSerializer = new CsvSerializer(null, null, null);
        csvSerializer.init(outputStream, DuzzyContext.DEFAULT.schemaContext());
        csvSerializer.writeAll(getDataOne());
        csvSerializer.writeAll(getDataTwo());

        assertThat(outputStream.toString()).isEqualTo(expected);
    }

    @Test
    void writeCsvWithCustomValues() throws IOException {
        final String expected = "1;\"one\"|2;\"two\"|";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final File serializerFile = getFromResources(getClass(), "serializer/csv-serializer.yaml");
        final CsvSerializer csvSerializer = YAML_MAPPER.readValue(serializerFile, CsvSerializer.class);
        csvSerializer.init(outputStream, DuzzyContext.DEFAULT.schemaContext());
        csvSerializer.writeAll(getDataOne());
        csvSerializer.writeAll(getDataTwo());

        assertThat(outputStream.toString()).isEqualTo(expected);
    }
}
