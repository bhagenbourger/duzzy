package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.deleteDirectory;
import static io.duzzy.tests.Helper.getFromResources;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.Duzzy;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.FileSink;
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
import java.util.Optional;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class LocalFileSinkTest {

  private static final String BUILD_MULTI_PATH = "build/multi";
  private static final String BUILD_SINGLE_PATH = "build/single";
  private static final String SCHEMA = "schema/duzzy-schema-three-columns.yaml";

  @AfterEach
  void afterEach() throws IOException {
    deleteDirectory(Paths.get(BUILD_MULTI_PATH));
    deleteDirectory(Paths.get(BUILD_SINGLE_PATH));
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
    final String filename = BUILD_SINGLE_PATH + "/test.json";
    final String expected =
        "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}\n{\"c1\":2,\"c2\":\"two\"}";

    final LocalFileSink localFileSink = new LocalFileSink(
        new JsonSerializer(),
        filename,
        true,
        null,
        null,
        null
    );
    localFileSink.init(new DuzzySchema(Optional.empty(), null));
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
  }

  @Test
  void writeXml() throws Exception {
    final String filename = BUILD_SINGLE_PATH + "/test.xml";
    final String expected =
        "<?xml version='1.0' encoding='UTF-8'?><rows>"
            + "<row><c1>1</c1><c2>one</c2></row>"
            + "<row><c1>2</c1><c2>two</c2></row>"
            + "<row><c1>2</c1><c2>two</c2></row></rows>";

    final LocalFileSink localFileSink = new LocalFileSink(
        new XmlSerializer("rows", "row"),
        filename,
        true,
        null,
        null,
        null
    );
    localFileSink.init(new DuzzySchema(Optional.empty(), null));
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
    assertThat(localFileSink.size()).isEqualTo(expected.length());
  }

  @ParameterizedTest
  @EnumSource(
      value = FileSink.CompressionAlgorithm.class,
      mode = EnumSource.Mode.EXCLUDE,
      names = {"NONE"}
  )
  void writeCompressedJson(FileSink.CompressionAlgorithm compressionAlgorithm) throws Exception {
    final String filename = BUILD_SINGLE_PATH + "/test.json" + "." + compressionAlgorithm.getName();
    final String expected =
        "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}\n{\"c1\":2,\"c2\":\"two\"}";

    final LocalFileSink localFileSink = new LocalFileSink(
        new JsonSerializer(),
        filename,
        true,
        compressionAlgorithm,
        null,
        null
    );
    localFileSink.init(new DuzzySchema(Optional.empty(), null));
    localFileSink.write(getDataOne());
    localFileSink.write(getDataTwo());
    localFileSink.write(getDataTwo());
    localFileSink.close();

    try (final CompressorInputStream compressorInputStream =
             new CompressorStreamFactory().createCompressorInputStream(
                 compressionAlgorithm.getName(),
                 Files.newInputStream(Path.of(filename))
             )) {
      assertThat(new String(compressorInputStream.readAllBytes(), UTF_8)).isEqualTo(expected);
    }
  }

  @Test
  void writeWithMultiThreads() throws Exception {
    final File schema = getFromResources(getClass(), SCHEMA);
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

    new Duzzy(schema, config, 1234L, 10L, null, null, 5, null).generate();

    final List<File> files = Arrays
        .stream(Objects.requireNonNull(new File(BUILD_MULTI_PATH).listFiles()))
        .sorted()
        .toList();
    assertThat(files).hasSize(5);
    assertThat(Files.readString(files.get(0).toPath())).isEqualTo(expected0);
    assertThat(Files.readString(files.get(1).toPath())).isEqualTo(expected1);
    assertThat(Files.readString(files.get(2).toPath())).isEqualTo(expected2);
    assertThat(Files.readString(files.get(3).toPath())).isEqualTo(expected3);
    assertThat(Files.readString(files.get(4).toPath())).isEqualTo(expected4);
  }

  @Test
  void writeWith100bPerFile() throws Exception {
    final File schema = getFromResources(getClass(), SCHEMA);
    final File config = getFromResources(getClass(), "config/duzzy-config-local-file-100B.yaml");
    final String expected0 =
        Files.readString(getFromResources(getClass(), "result/expected-size-0.xml").toPath());
    final String expected1 =
        Files.readString(getFromResources(getClass(), "result/expected-size-1.xml").toPath());
    final String expected2 =
        Files.readString(getFromResources(getClass(), "result/expected-size-2.xml").toPath());
    final String expected3 =
        Files.readString(getFromResources(getClass(), "result/expected-size-3.xml").toPath());
    final String expected4 =
        Files.readString(getFromResources(getClass(), "result/expected-size-4.xml").toPath());
    final String expected5 =
        Files.readString(getFromResources(getClass(), "result/expected-size-5.xml").toPath());
    final String expected6 =
        Files.readString(getFromResources(getClass(), "result/expected-size-6.xml").toPath());
    final String expected7 =
        Files.readString(getFromResources(getClass(), "result/expected-size-7.xml").toPath());
    final String expected8 =
        Files.readString(getFromResources(getClass(), "result/expected-size-8.xml").toPath());
    final String expected9 =
        Files.readString(getFromResources(getClass(), "result/expected-size-9.xml").toPath());

    new Duzzy(schema, config, 1234L, 10L, null, null, 1, null).generate();

    final List<File> files = Arrays
        .stream(Objects.requireNonNull(new File(BUILD_MULTI_PATH).listFiles()))
        .sorted()
        .toList();
    assertThat(files).hasSize(10);
    assertThat(Files.readString(files.get(0).toPath())).isEqualTo(expected0);
    assertThat(Files.readString(files.get(1).toPath())).isEqualTo(expected1);
    assertThat(Files.readString(files.get(2).toPath())).isEqualTo(expected2);
    assertThat(Files.readString(files.get(3).toPath())).isEqualTo(expected3);
    assertThat(Files.readString(files.get(4).toPath())).isEqualTo(expected4);
    assertThat(Files.readString(files.get(5).toPath())).isEqualTo(expected5);
    assertThat(Files.readString(files.get(6).toPath())).isEqualTo(expected6);
    assertThat(Files.readString(files.get(7).toPath())).isEqualTo(expected7);
    assertThat(Files.readString(files.get(8).toPath())).isEqualTo(expected8);
    assertThat(Files.readString(files.get(9).toPath())).isEqualTo(expected9);
  }

  @Test
  void writeWith100bPerFileAndThreads() throws Exception {
    final File schema = getFromResources(getClass(), SCHEMA);
    final File config = getFromResources(getClass(), "config/duzzy-config-local-file-100B.yaml");

    new Duzzy(schema, config, 1234L, 10L, null, null, 3, null).generate();

    final List<File> files = Arrays
        .stream(Objects.requireNonNull(new File(BUILD_MULTI_PATH).listFiles()))
        .sorted()
        .toList();
    assertThat(files).hasSize(30);
    assertThat(files.get(0).toPath().getFileName().toString()).isEqualTo("size_0_0.xml");
    assertThat(files.get(1).toPath().getFileName().toString()).isEqualTo("size_0_1.xml");
    assertThat(files.get(10).toPath().getFileName().toString()).isEqualTo("size_1_0.xml");
  }

  @Test
  void writeWith3rowsPerFile() throws Exception {
    final File schema = getFromResources(getClass(), SCHEMA);
    final File config = getFromResources(getClass(), "config/duzzy-config-local-file-3rows.yaml");
    final String expected0 =
        Files.readString(getFromResources(getClass(), "result/expected-row-0.xml").toPath());
    final String expected1 =
        Files.readString(getFromResources(getClass(), "result/expected-row-1.xml").toPath());
    final String expected2 =
        Files.readString(getFromResources(getClass(), "result/expected-row-2.xml").toPath());
    final String expected3 =
        Files.readString(getFromResources(getClass(), "result/expected-row-3.xml").toPath());

    new Duzzy(schema, config, 1234L, 10L, null, null, 1, null).generate();

    final List<File> files = Arrays
        .stream(Objects.requireNonNull(new File(BUILD_MULTI_PATH).listFiles()))
        .sorted()
        .toList();
    assertThat(files).hasSize(4);
    assertThat(Files.readString(files.get(0).toPath())).isEqualTo(expected0);
    assertThat(Files.readString(files.get(1).toPath())).isEqualTo(expected1);
    assertThat(Files.readString(files.get(2).toPath())).isEqualTo(expected2);
    assertThat(Files.readString(files.get(3).toPath())).isEqualTo(expected3);
  }

  @Test
  void writeWith3rowsPerFileAndThreads() throws Exception {
    final File schema = getFromResources(getClass(), SCHEMA);
    final File config = getFromResources(getClass(), "config/duzzy-config-local-file-3rows.yaml");

    new Duzzy(schema, config, 1234L, 10L, null, null, 3, null).generate();

    final List<File> files = Arrays
        .stream(Objects.requireNonNull(new File(BUILD_MULTI_PATH).listFiles()))
        .sorted()
        .toList();
    assertThat(files).hasSize(12);
    assertThat(files.get(0).toPath().getFileName().toString()).isEqualTo("row_0_0.xml");
    assertThat(files.get(1).toPath().getFileName().toString()).isEqualTo("row_0_1.xml");
    assertThat(files.get(11).toPath().getFileName().toString()).isEqualTo("row_2_3.xml");
  }
}
