package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.FileSink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

@Documentation(
    identifier = "io.duzzy.plugin.sink.HdfsSink",
    description = "Sink data to HDFS",
    module = "io.duzzy.plugin-hdfs",
    duzzyType = DuzzyType.SINK,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "core_site_file",
            aliases = {"coreSitePath", "core-site-file"},
            description = "The core-site.xml file"
        ),
        @Parameter(
            name = "hdfs_site_path",
            aliases = {"hdfsSiteFile", "hdfs-site-file"},
            description = "The hdfs-site.xml file"
        ),
        @Parameter(
            name = "filename",
            description = "The filename to write to"
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
        sink:
          identifier: "io.duzzy.plugin.sink.HdfsSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.CSVSerializer"
          coreSiteFile: "/path/to/core-site.xml"
          hdfsSitePath: "/path/to/hdfs-site.xml"
          filename: "/path/to/file"
          compressionAlgorithm: "NONE"
        """
)
public class HdfsSink extends FileSink {

  private final String coreSitePath;
  private final String hdfsSitePath;

  @JsonCreator
  public HdfsSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("core_site_file")
      @JsonAlias({"coreSitePath", "core-site-file"})
      String coreSitePath,
      @JsonProperty("hdfs_site_path")
      @JsonAlias({"hdfsSiteFile", "hdfs-site-file"})
      String hdfsSitePath,
      @JsonProperty("filename")
      String filename,
      @JsonProperty("compression_algorithm")
      @JsonAlias({"compressionAlgorithm", "compression-algorithm"})
      CompressionAlgorithm compressionAlgorithm,
      @JsonProperty("size_of_file")
      @JsonAlias({"sizeOfFile", "size-of-file"})
      Long sizeOfFile,
      @JsonProperty("rows_per_file")
      @JsonAlias({"rowsPerFile", "rows-per-file"})
      Long rowsPerFile
  ) {
    super(serializer, filename, compressionAlgorithm, sizeOfFile, rowsPerFile);
    this.coreSitePath = coreSitePath;
    this.hdfsSitePath = hdfsSitePath;
  }

  @Override
  protected OutputStream createOutputStream() throws IOException {
    final Configuration configuration = new Configuration();
    if (coreSitePath != null) {
      configuration.addResource(new Path(coreSitePath));
    }
    if (hdfsSitePath != null) {
      configuration.addResource(new Path(hdfsSitePath));
    }
    return FileSystem.get(configuration).create(new Path(incrementedName()));
  }

  @Override
  public HdfsSink fork(long id) throws Exception {
    return new HdfsSink(
        getSerializer().fork(id),
        coreSitePath,
        hdfsSitePath,
        forkedName(id),
        getCompressionAlgorithm(),
        getSizeOfFile(),
        getRowsPerFile()
    );
  }
}
