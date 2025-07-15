package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

public abstract class EventSink<P extends Closeable> extends Sink {

  private P producer;

  public EventSink(Serializer<?> serializer) {
    super(serializer);
  }

  protected abstract void sendEvent() throws IOException, ExecutionException, InterruptedException;

  protected abstract P buildProducer() throws IOException;

  @Override
  public OutputStream outputStreamSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  protected ByteArrayOutputStream getOutputStream() throws IOException {
    return (ByteArrayOutputStream) super.getOutputStream();
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    super.init(duzzySchema);
    this.producer = buildProducer();
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    reset();
    super.write(row);
    getSerializer().close();
    sendEvent();
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (producer != null) {
      producer.close();
    }
  }

  protected P getProducer() {
    return producer;
  }

  private void reset() throws IOException {
    if (getOutputStream().size() > 0) {
      getOutputStream().reset();
      getSerializer().reset();
    }
  }
}
