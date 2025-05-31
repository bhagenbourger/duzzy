package io.duzzy.plugin.sink;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.OutputStreamSink;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleSink extends OutputStreamSink {

  @JsonCreator
  public ConsoleSink(@JsonProperty("serializer") Serializer<?, OutputStream> serializer) {
    super(serializer);
  }

  @Override
  public OutputStream outputSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  public void close() throws Exception {
    serializer.close();
    System.out.println(((ByteArrayOutputStream) getOutput()).toString(UTF_8));
    //Don't close System.out
  }

  @Override
  public ConsoleSink fork(Long threadId) throws Exception {
    return new ConsoleSink(serializer.fork(threadId));
  }

  @Override
  public int maxThread() {
    return MONO_THREAD;
  }
}
