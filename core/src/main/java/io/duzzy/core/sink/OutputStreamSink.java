package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.serializer.Serializer;

import java.io.IOException;
import java.io.OutputStream;

public abstract class OutputStreamSink extends Sink {

    protected final OutputStream outputStream;

    public OutputStreamSink(Serializer<?> serializer, OutputStream outputStream) {
        super(serializer);
        this.outputStream = outputStream;
    }

    @Override
    public void init(SchemaContext schemaContext) throws IOException {
        this.serializer.init(outputStream, schemaContext);
    }

    @Override
    public void write(DataItems data) throws IOException {
        this.serializer.writeAll(data);
    }

    @Override
    public void close() throws IOException {
        super.close();
        outputStream.close();
    }
}
