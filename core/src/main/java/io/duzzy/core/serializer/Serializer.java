package io.duzzy.core.serializer;

import io.duzzy.core.DataItems;
import io.duzzy.core.Forkable;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.DuzzySchema;
import java.io.Closeable;
import java.io.IOException;

public abstract class Serializer<W extends Closeable, O>
    implements Plugin, Forkable<Serializer<W, O>> {
  private W writer;
  private O output;
  private DuzzySchema duzzySchema;

  public abstract long size();

  public abstract Boolean hasSchema();

  protected abstract W buildWriter(O output) throws IOException;

  protected abstract void serialize(DataItems data, W writer) throws IOException;

  public void serialize(DataItems data) throws IOException {
    if (writer == null) {
      throw new RuntimeException(
          "writer must be initiated "
              + "- call init(OutputStream outputStream, SchemaContext duzzySchema) method");
    }
    serialize(data, writer);
  }

  public void init(O output, DuzzySchema duzzySchema) throws IOException {
    this.duzzySchema = duzzySchema;
    this.output = output;
    reset();
  }

  public void close() throws IOException {
    if (writer != null) {
      writer.close();
    }
  }

  public void reset() throws IOException {
    writer = buildWriter(output);
  }

  public W getWriter() {
    return writer;
  }

  public O getOutput() {
    return output;
  }

  protected void setOutput(O output) {
    this.output = output;
  }

  protected DuzzySchema getDuzzySchema() {
    return duzzySchema;
  }

  protected void setDuzzySchema(DuzzySchema duzzySchema) {
    this.duzzySchema = duzzySchema;
  }
}
