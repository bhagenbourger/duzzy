package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

public class LocalFileSinkTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/local-file-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(LocalFileSink.class);
  }

  @Test
  void writeJson() throws Exception {
    final String filename = "build/test.json";
    final String expected =
        "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}\n{\"c1\":2,\"c2\":\"two\"}";

    final LocalFileSink localFileSink = new LocalFileSink(new JsonSerializer(), filename);
    localFileSink.init(DuzzyContext.DEFAULT.schemaContext());
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
  }
}
