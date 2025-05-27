package io.duzzy.plugin.sink;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

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

  @JsonCreator
  public ConsoleSink(@JsonProperty("serializer") Serializer<?> serializer) {
    super(serializer);
  }

  @Override
  public OutputStream outputStreamSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  public void close() throws Exception {
    getSerializer().close();
    System.out.println(((ByteArrayOutputStream) getOutputStream()).toString(UTF_8));
    //Don't close System.out
  }

  @Override
  public ConsoleSink fork(Long threadId) throws Exception {
    return new ConsoleSink(getSerializer().fork(threadId));
  }

  @Override
  public int maxThread() {
    return MONO_THREAD;
  }
}
