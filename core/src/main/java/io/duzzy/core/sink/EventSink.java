package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.DuzzyRowKey;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

public abstract class EventSink extends Sink {

  public EventSink(Serializer<?> serializer) {
    super(serializer);
  }

  protected abstract void sendEvent(DuzzyRowKey eventKey)
      throws IOException, ExecutionException, InterruptedException;

  @Override
  protected OutputStream outputStreamSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  protected ByteArrayOutputStream getOutputStream() throws IOException {
    return (ByteArrayOutputStream) super.getOutputStream();
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    super.init(duzzySchema);
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    reset();
    super.write(row);
    getSerializer().close();
    sendEvent(row.rowKey());
  }

  @Override
  public void close() throws Exception {
    super.close();
  }

  private void reset() throws IOException {
    if (getOutputStream().size() > 0) {
      getOutputStream().reset();
      getSerializer().reset();
    }
  }
}
