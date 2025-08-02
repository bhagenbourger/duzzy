package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.FileSink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Documentation(
    identifier = "io.duzzy.plugin.sink.LocalFileSink",
    description = "Write output into a local file",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.SINK,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "filename",
            description = "The name of the file to write"
        ),
        @Parameter(
            name = "create_if_not_exists",
            aliases = {"createIfNotExists", "create-if-not-exists"},
            description = "Create the file if it does not exist",
            defaultValue = "false"
        ),
        @Parameter(
            name = "compression_algorithm",
            aliases = {"compressionAlgorithm", "compression-algorithm"},
            description = "The compression algorithm to use for the file, "
                + "available options are: NONE, BZIP2, GZIP, ZSTD. "
                + "If not specified, no compression will be applied.",
            defaultValue = "NONE"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.sink.LocalFileSink"
        filename: "output.csv.gz"
        create_if_not_exists: true
        compression_algorithm: "GZIP"
        serializer:
          identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class LocalFileSink extends FileSink {

  private final String filename;
  private final Boolean createIfNotExists;


  @JsonCreator
  public LocalFileSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("filename")
      String filename,
      @JsonProperty("create_if_not_exists")
      @JsonAlias({"createIfNotExists", "create-if-not-exists"})
      Boolean createIfNotExists,
      @JsonProperty("compression_algorithm")
      @JsonAlias({"compressionAlgorithm", "compression-algorithm"})
      CompressionAlgorithm compressionAlgorithm
  ) throws IOException {
    super(serializer, compressionAlgorithm);
    this.filename = filename;
    this.createIfNotExists = createIfNotExists;
  }

  @Override
  protected OutputStream outputStreamSupplier() throws IOException {
    if (createIfNotExists != null && createIfNotExists) {
      final Path folder = Path.of(filename).getParent();
      if (folder != null && !Files.exists(folder)) {
        Files.createDirectories(folder);
      }
    }
    return new FileOutputStream(filename);
  }

  @Override
  public LocalFileSink fork(Long threadId) throws Exception {
    return new LocalFileSink(
        getSerializer().fork(threadId),
        addFilePart(filename, threadId),
        createIfNotExists,
        getCompressionAlgorithm()
    );
  }
}
