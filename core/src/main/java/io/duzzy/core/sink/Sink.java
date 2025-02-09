package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Sink implements Plugin, Forkable<Sink> {

  private OutputStream outputStream;
  protected final Serializer<?> serializer;

  public Sink(Serializer<?> serializer) {
    this.serializer = serializer;
  }

  public abstract OutputStream outputStreamSupplier() throws IOException;

  public void init(SchemaContext schemaContext) throws Exception {
    this.serializer.init(getOutputStream(), schemaContext);
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
