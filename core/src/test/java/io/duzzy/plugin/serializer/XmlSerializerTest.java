package io.duzzy.plugin.serializer;

import io.duzzy.core.Serializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class XmlSerializerTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File serializerFile = getFromResources(getClass(), "serializer/xml-serializer.yaml");
        final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

        assertThat(serializer).isInstanceOf(XmlSerializer.class);
    }

    @Test
    void writeXmlWithDefaultValues() throws IOException {
        final String expected = "<?xml version='1.0' encoding='UTF-8'?><rows><row><c1>1</c1><c2>one</c2></row><row><c1>2</c1><c2>two</c2></row></rows>";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final XmlSerializer xmlSerializer = new XmlSerializer(null, null);
        xmlSerializer.init(outputStream, List.of());
        xmlSerializer.writeAll(getDataOne());
        xmlSerializer.writeAll(getDataTwo());
        xmlSerializer.close();

        assertThat(outputStream.toString()).isEqualTo(expected);
    }

    @Test
    void writXmlWithCustomValues() throws IOException {
        final String expected = "<?xml version='1.0' encoding='UTF-8'?><myRoot><myRow><c1>1</c1><c2>one</c2></myRow><myRow><c1>2</c1><c2>two</c2></myRow></myRoot>";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final File serializerFile = getFromResources(getClass(), "serializer/xml-serializer.yaml");
        final XmlSerializer xmlSerializer = YAML_MAPPER.readValue(serializerFile, XmlSerializer.class);
        xmlSerializer.init(outputStream, List.of());
        xmlSerializer.writeAll(getDataOne());
        xmlSerializer.writeAll(getDataTwo());
        xmlSerializer.close();

        assertThat(outputStream.toString()).isEqualTo(expected);
    }
}
