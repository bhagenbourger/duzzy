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
        ),
        @Parameter(
            name = "size_of_file",
            aliases = {"sizeOfFile", "size-of-file"},
            description = "The size of the file in bytes. "
                + "If not specified, the file will not be limited by size."
        ),
        @Parameter(
            name = "rows_per_file",
            aliases = {"rowsPerFile", "rows-per-file"},
            description = "The number of rows per file. "
                + "If not specified, the file will not be limited by number of rows."
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
      CompressionAlgorithm compressionAlgorithm,
      @JsonProperty("size_of_file")
      @JsonAlias({"sizeOfFile", "size-of-file"})
      Long sizeOfFile,
      @JsonProperty("rows_per_file")
      @JsonAlias({"rowsPerFile", "rows-per-file"})
      Long rowsPerFile
  ) throws IOException {
    super(serializer, filename, compressionAlgorithm, sizeOfFile, rowsPerFile);
    this.createIfNotExists = createIfNotExists;
  }

  @Override
  public LocalFileSink fork(long id) throws Exception {
    return new LocalFileSink(
        getSerializer().fork(id),
        forkedName(id),
        createIfNotExists,
        getCompressionAlgorithm(),
        getSizeOfFile(),
        getRowsPerFile()
    );
  }

  @Override
  protected OutputStream createOutputStream() throws IOException {
    final String fileName = incrementedName();
    if (createIfNotExists != null && createIfNotExists) {
      final Path folder = Path.of(fileName).getParent();
      if (folder != null && !Files.exists(folder)) {
        Files.createDirectories(folder);
      }
    }
    return new FileOutputStream(fileName);
  }
}
