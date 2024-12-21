package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;

public abstract class Sink implements Plugin {

  protected final Serializer<?> serializer;

  public Sink(Serializer<?> serializer) {
    this.serializer = serializer;
  }

  public abstract void init(SchemaContext schemaContext) throws IOException;

  public abstract void write(DataItems data) throws IOException;

  public void close() throws IOException {
    serializer.close();
  }

  public Serializer<?> getSerializer() {
    return serializer;
  }
}
