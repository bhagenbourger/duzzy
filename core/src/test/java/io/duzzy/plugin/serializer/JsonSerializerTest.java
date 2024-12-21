package io.duzzy.plugin.serializer;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.serializer.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class JsonSerializerTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File serializerFile = getFromResources(getClass(), "serializer/json-serializer.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

    assertThat(serializer).isInstanceOf(JsonSerializer.class);
  }

  @Test
  void serializeJson() throws IOException {
    final String expected = "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}";
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    final JsonSerializer jsonSerializer = new JsonSerializer();
    jsonSerializer.init(outputStream, DuzzyContext.DEFAULT.schemaContext());
    jsonSerializer.serializeAll(getDataOne());
    jsonSerializer.serializeAll(getDataTwo());

    assertThat(outputStream.toString()).isEqualTo(expected);
  }
}
