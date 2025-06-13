package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
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
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.sink.LocalFileSink"
        filename: "output.csv"
        create_if_not_exists: true
        serializer:
          identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class LocalFileSink extends Sink {

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
      Boolean createIfNotExists
  ) throws IOException {
    super(serializer);
    this.filename = filename;
    this.createIfNotExists = createIfNotExists;
  }

  @Override
  public OutputStream outputStreamSupplier() throws IOException {
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
