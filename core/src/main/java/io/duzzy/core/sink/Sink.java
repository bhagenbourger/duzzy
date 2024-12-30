package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.Plugin;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.serializer.Serializer;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Sink implements Plugin {

  protected final Serializer<?> serializer;
  protected final OutputStream outputStream;

  public Sink(Serializer<?> serializer, OutputStream outputStream) {
    this.serializer = serializer;
    this.outputStream = outputStream;
  }

  public Optional<List<String>> checkArguments() {
    final List<String> errors = new ArrayList<>();
    if (serializer == null) {
      errors.add("Serializer is null in Sink " + getIdentifier());
    }
    if (outputStream == null) {
      errors.add("Output stream is null in Sink " + getIdentifier());
    }
    return Optional.of(errors).filter(e -> !e.isEmpty());
  }

  public void init(SchemaContext schemaContext) throws Exception {
    this.serializer.init(outputStream, schemaContext);
  }

  public void write(DataItems data) throws Exception {
    this.serializer.serialize(data);
  }

  public void close() throws Exception {
    serializer.close();
    outputStream.close();
  }

  public Serializer<?> getSerializer() {
    return serializer;
  }
}
