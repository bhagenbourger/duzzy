package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ConsoleSink extends Sink {

  @JsonCreator
  public ConsoleSink(@JsonProperty("serializer") Serializer<?> serializer) {
    super(serializer, new ByteArrayOutputStream());
  }

  @Override
  public void close() throws IOException {
    serializer.close();
    System.out.writeBytes(((ByteArrayOutputStream) outputStream).toByteArray());
    System.out.println();
    //Don't close System.out
  }
}
