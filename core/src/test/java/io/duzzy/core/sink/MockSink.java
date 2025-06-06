package io.duzzy.core.sink;

import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MockSink extends OutputStreamSink {

  public MockSink() {
    super(new JsonSerializer());
  }

  @Override
  public OutputStream outputSupplier() throws IOException {
    return new ByteArrayOutputStream();
  }

  @Override
  public OutputStreamSink fork(Long threadId) {
    return new MockSink();
  }

  @Override
  public int maxThread() {
    return 5;
  }
}
