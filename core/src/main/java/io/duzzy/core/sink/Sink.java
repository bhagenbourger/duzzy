package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.serializer.JsonSerializer;

public abstract class Sink implements Plugin, Forkable<Sink> {

  private final Serializer<?> serializer;

  public Sink(Serializer<?> serializer) {
    this.serializer = serializer == null ? new JsonSerializer() : serializer;
  }

  public abstract long size();

  public abstract void init(DuzzySchema duzzySchema) throws Exception;

  public void write(DuzzyRow row) throws Exception {
    this.serializer.serialize(row);
  }

  public void close() throws Exception {
    serializer.close();
  }

  public boolean hasSchema() {
    return serializer.hasSchema();
  }

  protected Serializer<?> getSerializer() {
    return serializer;
  }
}
