package io.duzzy.plugin.serializer;

import org.apache.avro.io.BinaryEncoder;

import java.io.Closeable;
import java.io.IOException;

public record BinaryEncoderCloseable(BinaryEncoder binaryEncoder) implements Closeable {

    @Override
    public void close() throws IOException {
        binaryEncoder.flush();
    }
}
