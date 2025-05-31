package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.OutputStreamSink;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFileSink extends OutputStreamSink {

  private final String filename;
  private final Boolean createIfNotExists;


  @JsonCreator
  public LocalFileSink(
      @JsonProperty("serializer")
      Serializer<?, OutputStream> serializer,
      @JsonProperty("filename")
      String filename,
      @JsonProperty("create_if_not_exists")
      @JsonAlias({"createIfNotExists", "create-if-not-exists"})
      Boolean createIfNotExists
  ) throws IOException {
    super(serializer);
    this.filename = filename;
    this.createIfNotExists = createIfNotExists;
  }

  @Override
  public OutputStream outputSupplier() throws IOException {
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
        serializer.fork(threadId),
        addFilePart(filename, threadId),
        createIfNotExists
    );
  }
}
