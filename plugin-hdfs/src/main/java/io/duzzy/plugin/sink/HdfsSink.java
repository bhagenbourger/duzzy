package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsSink extends Sink {

  private final String coreSitePath;
  private final String hdfsSitePath;
  private final String filename;

  @JsonCreator
  public HdfsSink(
      @JsonProperty("serializer") Serializer<?> serializer,
      @JsonProperty("coreSiteFile") String coreSitePath,
      @JsonProperty("hdfsSitePath") String hdfsSitePath,
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
        serializer.fork(threadId),
        coreSitePath,
        hdfsSitePath,
        addFilePart(filename, threadId)
    );
  }
}
