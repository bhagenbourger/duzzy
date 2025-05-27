package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
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
        """
)
public class HdfsSink extends Sink {

  private final String coreSitePath;
  private final String hdfsSitePath;
  private final String filename;

  @JsonCreator
  public HdfsSink(
      @JsonProperty("serializer") Serializer<?> serializer,
      @JsonProperty("core_site_file")
      @JsonAlias({"coreSitePath", "core-site-file"})
      String coreSitePath,
      @JsonProperty("hdfs_site_path")
      @JsonAlias({"hdfsSiteFile", "hdfs-site-file"})
      String hdfsSitePath,
      @JsonProperty("filename") String filename
  ) {
    super(serializer);
    this.coreSitePath = coreSitePath;
    this.hdfsSitePath = hdfsSitePath;
    this.filename = filename;
  }

  @Override
  public OutputStream outputStreamSupplier() throws IOException {
    final Configuration configuration = new Configuration();
    if (coreSitePath != null) {
      configuration.addResource(new Path(coreSitePath));
    }
    if (hdfsSitePath != null) {
      configuration.addResource(new Path(hdfsSitePath));
    }
    return FileSystem.get(configuration).create(new Path(filename));
  }

  @Override
  public HdfsSink fork(Long threadId) throws Exception {
    return new HdfsSink(
        getSerializer().fork(threadId),
        coreSitePath,
        hdfsSitePath,
        addFilePart(filename, threadId)
    );
  }
}
