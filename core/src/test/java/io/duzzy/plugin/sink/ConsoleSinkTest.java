package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ConsoleSinkTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/console-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(ConsoleSink.class);
  }

  @Test
  void writeJson() throws Exception {
    final String expected =
        "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}\n{\"c1\":1,\"c2\":\"one\"}\n";
    final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor, true, StandardCharsets.UTF_8));

    final ConsoleSink consoleSink = new ConsoleSink(new JsonSerializer());
    consoleSink.init(new DuzzySchema(Optional.empty(), null));
    consoleSink.write(getDataOne());
    consoleSink.write(getDataTwo());
    consoleSink.write(getDataOne());
    consoleSink.close();

    assertThat(outputStreamCaptor.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }
}
