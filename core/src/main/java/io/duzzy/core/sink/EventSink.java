package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.DuzzyRowKey;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public abstract class EventSink extends Sink {

  private final ByteArrayOutputStream outputStream;
  private long currentSize = 0;

  public EventSink(Serializer<?> serializer) {
    super(serializer);
    this.outputStream = new ByteArrayOutputStream();
  }

  protected abstract void sendEvent(DuzzyRowKey eventKey, ByteArrayOutputStream outputStream)
      throws IOException, ExecutionException, InterruptedException;

  @Override
  public long size() {
    return currentSize;
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    getSerializer().init(outputStream, duzzySchema);
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    reset();
    super.write(row);
    getSerializer().close();
    sendEvent(row.rowKey(), outputStream);
  }

  @Override
  public void close() throws Exception {
    super.close();
    currentSize += outputStream.size();
    outputStream.close();
  }

  private void reset() throws Exception {
    if (outputStream.size() > 0) {
      currentSize += outputStream.size();
      outputStream.reset();
      init(null);
    }
  }
}
