package io.duzzy.core.serializer;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Serializer<W extends AutoCloseable>
    implements Plugin, Forkable<Serializer<W>> {

  private W writer;
  private DuzzySchema duzzySchema;

  public abstract Boolean hasSchema();

  protected abstract W buildWriter(OutputStream outputStream) throws IOException;

  protected abstract void serialize(DuzzyRow row, W writer) throws IOException;

  public void serialize(DuzzyRow row) throws IOException {
    if (writer == null) {
      throw new RuntimeException("writer must be initiated "
          + "- call init(OutputStream outputStream, SchemaContext duzzySchema) method");
    }
    serialize(row, writer);
  }

  public void init(OutputStream outputStream, DuzzySchema duzzySchema) throws Exception {
    if (duzzySchema != null) {
      this.duzzySchema = duzzySchema;
    }
    close();
    writer = buildWriter(outputStream);
  }

  public void close() throws Exception {
    if (writer != null) {
      writer.close();
    }
  }

  protected DuzzySchema getDuzzySchema() {
    return duzzySchema;
  }

  protected W getWriter() {
    return writer;
  }
}
