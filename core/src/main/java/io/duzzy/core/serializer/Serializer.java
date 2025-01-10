package io.duzzy.core.serializer;

import io.duzzy.core.DataItems;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Serializer<W extends Closeable> implements Plugin, Forkable<Serializer<W>> {

  private W writer;
  private DuzzySchema duzzySchema;
  private OutputStream outputStream;

  public abstract Boolean hasSchema();

  protected abstract W buildWriter(OutputStream outputStream) throws IOException;

  protected abstract void serialize(DataItems data, W writer) throws IOException;

  public void serialize(DataItems data) throws IOException {
    if (writer == null) {
      throw new RuntimeException(
          "writer must be initiated "
              + "- call init(OutputStream outputStream, SchemaContext duzzySchema) method");
    }
    serialize(data, writer);
  }

  public void init(OutputStream outputStream, DuzzySchema duzzySchema) throws IOException {
    this.duzzySchema = duzzySchema;
    this.outputStream = outputStream;
    reset();
  }

  public void close() throws IOException {
    if (writer != null) {
      writer.close();
    }
  }

  public void reset() throws IOException {
    writer = buildWriter(outputStream);
  }

  protected DuzzySchema getDuzzySchema() {
    return duzzySchema;
  }
}
