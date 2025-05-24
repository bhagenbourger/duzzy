package io.duzzy.core.serializer;

import io.duzzy.core.schema.DuzzySchema;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputStreamSerializer<W extends AutoCloseable>
    extends Serializer<W, OutputStream> {

  @Override
  public void init(OutputStream output, DuzzySchema duzzySchema, Long rowCount) throws IOException {
    super.init(new DataOutputStream(output), duzzySchema, rowCount);
  }

  @Override
  public long size(OutputStream output, W writer) {
    return output == null ? 0 : ((DataOutputStream) output).size();
  }

}
