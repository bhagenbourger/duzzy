package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
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

  protected abstract void sendEvent(ByteArrayOutputStream outputStream)
      throws ExecutionException, InterruptedException, IOException;

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
    sendEvent(outputStream);
  }

  @Override
  public void close() throws Exception {
    super.close();
    outputStream.close();
  }

  private void reset() throws Exception {
    if (outputStream.size() > 0) {
      currentSize += outputStream.size();
      outputStream.reset();
      getSerializer().initWriter(outputStream); //TODO: check if this is needed
    }
  }
}
