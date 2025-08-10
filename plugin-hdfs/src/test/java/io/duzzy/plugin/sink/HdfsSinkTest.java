package io.duzzy.plugin.sink;

import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.FileSink;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class HdfsSinkTest {

  @Test
  void writeJsonFile() throws Exception {
    final String expected = "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}";
    final String filename = "build/hdfs.json";

    final HdfsSink hdfsSink = new HdfsSink(
        new JsonSerializer(),
        null,
        null,
        filename,
        FileSink.CompressionAlgorithm.NONE
    );
    hdfsSink.init(new DuzzySchema(Optional.empty(), null));
    hdfsSink.write(getDataOne());
    hdfsSink.write(getDataTwo());
    hdfsSink.close();

    assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
  }

  @ParameterizedTest
  @EnumSource(
      value = FileSink.CompressionAlgorithm.class,
      mode = EnumSource.Mode.EXCLUDE,
      names = {"NONE"}
  )
  void writeCompressedJson(FileSink.CompressionAlgorithm compressionAlgorithm) throws Exception {
    final String expected = "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}";
    final String filename = "build/hdfs.json" + "." + compressionAlgorithm.getName();

    final HdfsSink hdfsSink = new HdfsSink(
        new JsonSerializer(),
        null,
        null,
        filename,
        compressionAlgorithm
    );
    hdfsSink.init(new DuzzySchema(Optional.empty(), null));
    hdfsSink.write(getDataOne());
    hdfsSink.write(getDataTwo());
    hdfsSink.close();

    try (final CompressorInputStream compressorInputStream =
             new CompressorStreamFactory().createCompressorInputStream(
                 compressionAlgorithm.getName(),
                 Files.newInputStream(Path.of(filename))
             )) {
      assertThat(new String(compressorInputStream.readAllBytes(), UTF_8)).isEqualTo(expected);
    }
  }
}
