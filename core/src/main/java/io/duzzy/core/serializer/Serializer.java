package io.duzzy.core.serializer;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import java.io.IOException;

public abstract class Serializer<W extends AutoCloseable, O>
    implements Plugin, Forkable<Serializer<W, O>> {
  private W writer;
  private O output;
  private DuzzySchema duzzySchema;
  private Long rowCount = 0L;

  public long size() {
    return size(output, writer);
  }

  protected abstract long size(O output, W writer);

  public abstract Boolean hasSchema();

  protected abstract W buildWriter(O output) throws IOException;

  protected abstract void serialize(DuzzyRow row, W writer) throws IOException;

  public void serialize(DuzzyRow row) throws IOException {
    if (writer == null) {
      throw new RuntimeException("writer must be initiated "
          + "- call init(OutputStream outputStream, SchemaContext duzzySchema) method");
    }
    serialize(row, writer);
  }

  public void init(O output, DuzzySchema duzzySchema, Long rowCount) throws IOException {
    this.duzzySchema = duzzySchema;
    this.output = output;
    this.rowCount = rowCount;
    reset();
  }

  public void close() throws Exception {
    if (writer != null) {
      writer.close();
    }
  }

  public void reset() throws IOException {
    writer = buildWriter(output);
  }

  protected W getWriter() {
    return writer;
  }

  protected DuzzySchema getDuzzySchema() {
    return duzzySchema;
  }
}
