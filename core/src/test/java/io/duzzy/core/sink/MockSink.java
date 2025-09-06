package io.duzzy.core.sink;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.ByteArrayOutputStream;

public class MockSink extends Sink {

  private final ByteArrayOutputStream outputStream;

  public MockSink() {
    super(new JsonSerializer());
    this.outputStream = new ByteArrayOutputStream();
  }

  @Override
  public long size() {
    return outputStream.size();
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    getSerializer().init(outputStream, duzzySchema);
  }

  @Override
  public void close() throws Exception {
    super.close();
    outputStream.close();
  }

  @Override
  public Sink fork(long id) {
    return new MockSink();
  }

  @Override
  public int maxThread() {
    return 5;
  }
}
