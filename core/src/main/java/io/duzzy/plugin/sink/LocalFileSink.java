package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LocalFileSink extends Sink {

  @JsonCreator
  public LocalFileSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("filename")
      String filename,
      @JsonProperty("create_if_not_exists")
      @JsonAlias({"createIfNotExists", "create-if-not-exists"})
      Boolean createIfNotExists
  ) throws IOException {
    super(serializer, buildFileOutputStream(filename, createIfNotExists));
  }

  private static FileOutputStream buildFileOutputStream(
      String filename,
      Boolean createIfNotExists
  ) throws IOException {
    if (createIfNotExists != null && createIfNotExists) {
      final Path folder = Path.of(filename).getParent();
      if (folder != null && !Files.exists(folder)) {
        Files.createDirectories(folder);
      }
    }
    return new FileOutputStream(filename);
  }
}
