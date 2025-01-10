package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Sink implements Plugin, Forkable<Sink> {

  private OutputStream outputStream;
  protected final Serializer<?> serializer;

  public Sink(Serializer<?> serializer) {
    this.serializer = serializer == null ? new JsonSerializer() : serializer;
  }

  public abstract OutputStream outputStreamSupplier() throws IOException;

  public void init(DuzzySchema duzzySchema) throws Exception {
    this.serializer.init(getOutputStream(), duzzySchema);
  }

  public void write(DataItems data) throws Exception {
    this.serializer.serialize(data);
  }

  public void close() throws Exception {
    serializer.close();
    getOutputStream().close();
  }

  protected OutputStream getOutputStream() throws IOException {
    if (outputStream == null) {
      outputStream = outputStreamSupplier();
    }
    return outputStream;
  }

  public Serializer<?> getSerializer() {
    return serializer;
  }
}
