package io.duzzy.plugin.sink;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.ByteArrayOutputStream;

@Documentation(
    identifier = "io.duzzy.plugin.sink.ConsoleSink",
    description = "Print output into the console",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.SINK,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.sink.ConsoleSink"
        serializer:
          identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class ConsoleSink extends Sink {

  private final ByteArrayOutputStream outputStream;
  private long currentSize = 0;

  @JsonCreator
  public ConsoleSink(@JsonProperty("serializer") Serializer<?> serializer) {
    super(serializer);
    this.outputStream = new ByteArrayOutputStream();
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    getSerializer().init(outputStream, duzzySchema);
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    super.write(row);
    currentSize += outputStream.size();
    System.out.print(outputStream.toString(UTF_8));
    outputStream.reset();
  }

  @Override
  public long size() {
    return currentSize;
  }

  @Override
  public void close() throws Exception {
    super.close();
    System.out.println(outputStream.toString(UTF_8));
    outputStream.close();
  }

  @Override
  public ConsoleSink fork(long id) throws Exception {
    return new ConsoleSink(getSerializer().fork(id));
  }

  @Override
  public int maxThread() {
    return MONO_THREAD;
  }
}
