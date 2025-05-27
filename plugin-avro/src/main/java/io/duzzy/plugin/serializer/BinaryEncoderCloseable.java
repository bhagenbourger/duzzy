package io.duzzy.plugin.serializer;

import java.io.IOException;
import org.apache.avro.io.BinaryEncoder;

public record BinaryEncoderCloseable(BinaryEncoder binaryEncoder) implements AutoCloseable {

  @Override
  public void close() throws IOException {
    binaryEncoder.flush();
  }
}
