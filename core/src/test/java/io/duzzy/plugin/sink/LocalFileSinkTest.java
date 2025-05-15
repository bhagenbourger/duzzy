package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.deleteDirectory;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.Duzzy;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.serializer.XmlSerializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class LocalFileSinkTest {

  @AfterAll
  static void afterAll() throws IOException {
    deleteDirectory(Paths.get("build/multi"));
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/local-file-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(LocalFileSink.class);
  }

  @Test
  void initFailed() throws Exception {
    final File sinkFile = getFromResources(getClass(), "sink/local-file-sink-error.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThatThrownBy(() -> sink.init(null))
        .isInstanceOf(FileNotFoundException.class)
        .hasMessage("build/failed/test.json (No such file or directory)");
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
    localFileSink.init(new DuzzySchema(null));
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
  }

  @Test
  void writeXml() throws Exception {
    final String filename = "build/test.xml";
    final String expected =
        "<?xml version='1.0' encoding='UTF-8'?><rows>"
            + "<row><c1>1</c1><c2>one</c2></row>"
            + "<row><c1>2</c1><c2>two</c2></row>"
            + "<row><c1>2</c1><c2>two</c2></row></rows>";

    final LocalFileSink localFileSink = new LocalFileSink(
        new XmlSerializer("rows", "row"),
        filename,
        true
    );
    localFileSink.init(new DuzzySchema(null));
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
    assertThat(localFileSink.getSerializer().size()).isEqualTo(expected.length());
  }

  @Test
  void writeWithMultiThreads() throws Exception {
    final File schema = getFromResources(getClass(), "schema/duzzy-schema-three-columns.yaml");
    final File config = getFromResources(getClass(), "config/duzzy-config-local-file.yaml");
    final String expected0 =
        Files.readString(getFromResources(getClass(), "result/expected-multi-0.xml").toPath());
    final String expected1 =
        Files.readString(getFromResources(getClass(), "result/expected-multi-1.xml").toPath());
    final String expected2 =
        Files.readString(getFromResources(getClass(), "result/expected-multi-2.xml").toPath());
    final String expected3 =
        Files.readString(getFromResources(getClass(), "result/expected-multi-3.xml").toPath());
    final String expected4 =
        Files.readString(getFromResources(getClass(), "result/expected-multi-4.xml").toPath());

    new Duzzy(schema, config, 1234L, 50L, 5, null).generate();

    final List<File> files = Arrays
        .stream(Objects.requireNonNull(new File("build/multi").listFiles()))
        .sorted()
        .toList();
    assertThat(files).hasSize(5);
    assertThat(Files.readString(files.get(0).toPath())).isEqualTo(expected0);
    assertThat(Files.readString(files.get(1).toPath())).isEqualTo(expected1);
    assertThat(Files.readString(files.get(2).toPath())).isEqualTo(expected2);
    assertThat(Files.readString(files.get(3).toPath())).isEqualTo(expected3);
    assertThat(Files.readString(files.get(4).toPath())).isEqualTo(expected4);
  }
}
