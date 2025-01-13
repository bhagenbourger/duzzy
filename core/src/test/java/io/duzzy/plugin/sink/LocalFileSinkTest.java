package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.File;
import java.io.IOException;
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
  void parsedFromYamlFailed() {
    final File sinkFile = getFromResources(getClass(), "sink/local-file-sink-error.yaml");

    assertThatThrownBy(() -> YAML_MAPPER.readValue(sinkFile, Sink.class))
        .isInstanceOf(ValueInstantiationException.class)
        .hasMessage("Cannot construct instance of `io.duzzy.plugin.sink.LocalFileSink`, "
            + "problem: build/failed/test.json (No such file or directory)"
            + "\n at [Source: UNKNOWN; byte offset: #UNKNOWN]");
  }

  @Test
  void writeJson() throws Exception {
    final String filename = "build/test.json";
    final String expected =
        "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}\n{\"c1\":2,\"c2\":\"two\"}";

    final LocalFileSink localFileSink = new LocalFileSink(
        new JsonSerializer(),
        filename,
        true
    );
    localFileSink.init(new SchemaContext(null));
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
  }
}
