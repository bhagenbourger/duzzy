package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;

public abstract class Sink<O> implements Plugin, Forkable<Sink<O>> {

  private O output;
  protected final Serializer<?, O> serializer;

  public Sink(Serializer<?, O> serializer) {
    this.serializer = serializer;
  }

  public abstract O outputSupplier() throws IOException;

  public void init(DuzzySchema duzzySchema) throws Exception {
    this.serializer.init(getOutput(), duzzySchema);
  }

  public void write(DataItems data) throws Exception {
    this.serializer.serialize(data);
  }

  public void close() throws Exception {
    serializer.close();
  }

  public Serializer<?, O> getSerializer() {
    return serializer;
  }

  protected O getOutput() throws IOException {
    if (output == null) {
      output = outputSupplier();
    }
    return output;
  }
}
