package io.duzzy.core.serializer;

import io.duzzy.core.DataItems;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.SchemaContext;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Serializer<W extends Closeable> implements Plugin {

  private W writer;
  private SchemaContext schemaContext;

  protected abstract W buildWriter(OutputStream outputStream) throws IOException;

  protected abstract void serialize(DataItems data, W writer) throws IOException;

  public abstract Boolean hasSchema();

  public void init(OutputStream outputStream, SchemaContext schemaContext) throws IOException {
    this.schemaContext = schemaContext;
    this.writer = buildWriter(outputStream);
  }

  public void serializeAll(DataItems data) throws IOException {
    if (writer == null) {
      throw new RuntimeException(
          "writer must be initiated "
              + "- call init(OutputStream outputStream, SchemaContext schemaContext) method");
    }
    serialize(data, writer);
  }

  public void serializeUnit(DataItems data, OutputStream outputStream) throws IOException {
    W localWriter = buildWriter(outputStream);
    serialize(data, localWriter);
    localWriter.close();
  }

  public void close() throws IOException {
    if (writer != null) {
      writer.close();
    }
  }

  protected SchemaContext getSchemaContext() {
    return schemaContext;
  }
}
