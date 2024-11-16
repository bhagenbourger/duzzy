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

public class JsonSerializerTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File serializerFile = getFromResources(getClass(), "serializer/json-serializer.yaml");
        final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

        assertThat(serializer).isInstanceOf(JsonSerializer.class);
    }

    @Test
    void writeJson() throws IOException {
        final String expected = "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final JsonSerializer jsonSerializer = new JsonSerializer();
        jsonSerializer.init(outputStream, List.of());
        jsonSerializer.writeAll(getDataOne());
        jsonSerializer.writeAll(getDataTwo());

        assertThat(outputStream.toString()).isEqualTo(expected);
    }
}
