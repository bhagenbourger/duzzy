package io.duzzy.core.serializer;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class Serializer<W extends Closeable> implements Plugin, Forkable<Serializer<W>> {

  private W writer;
  private DuzzySchema duzzySchema;
  private DataOutputStream outputStream;
  private Long rowCount = 0L;

  public long size() {
    return size(outputStream, writer);
  }

  protected long size(OutputStream outputStream, W writer) {
    return outputStream instanceof DataOutputStream ? ((DataOutputStream) outputStream).size() : 0;
  }

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

  public void init(OutputStream outputStream,
                   DuzzySchema duzzySchema,
                   Long rowCount
  ) throws IOException {
    this.duzzySchema = duzzySchema;
    this.outputStream = new DataOutputStream(outputStream);
    this.rowCount = rowCount;
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
