package io.duzzy.core.serializer;

import io.duzzy.core.schema.DuzzySchema;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputStreamSerializer<W extends Closeable>
    extends Serializer<W, OutputStream> {

  @Override
  public void init(OutputStream outputStream, DuzzySchema duzzySchema) throws IOException {
    super.setDuzzySchema(duzzySchema);
    super.setOutput(new DataOutputStream(outputStream));
    reset();
  }

  @Override
  public long size() {
    return ((DataOutputStream) getOutput()).size();
  }

}
