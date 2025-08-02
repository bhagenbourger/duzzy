package io.duzzy.core.sink;

import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MockSink extends Sink {

  public MockSink() {
    super(new JsonSerializer());
  }

  @Override
  protected OutputStream outputStreamSupplier() throws IOException {
    return new ByteArrayOutputStream();
  }

  @Override
  public Sink fork(Long threadId) {
    return new MockSink();
  }

  @Override
  public int maxThread() {
    return 5;
  }
}
