package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.SqlSerializer;
import io.duzzy.tests.Data;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DuckdbSinkTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/duckdb-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(DuckdbSink.class);
    assertThat(sink.getSerializer()).isInstanceOf(SqlSerializer.class);
  }

  @Test
  void writeData() throws Exception {
    final DuckdbSink duckdbSink = new DuckdbSink(
        new SqlSerializer("test"),
        "jdbc:duckdb:/tmp/my_database",
        null,
        null,
        null
    );

    duckdbSink.init(null);

    duckdbSink.write(Data.getDataOne());
    duckdbSink.write(Data.getDataOne());
    duckdbSink.write(Data.getDataOne());
  }
}
