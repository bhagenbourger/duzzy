package io.duzzy.core.sink;

import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.OutputStream;

public abstract class OutputStreamSink extends Sink<OutputStream> {

  public OutputStreamSink(Serializer<?, OutputStream> serializer) {
    super(serializer == null ? new JsonSerializer() : serializer);
  }

  @Override
  public void close() throws Exception {
    super.close();
    getOutput().close();
  }
}
