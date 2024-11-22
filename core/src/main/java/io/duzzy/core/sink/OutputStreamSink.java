package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.Serializer;
import io.duzzy.core.column.Column;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class OutputStreamSink extends Sink {

    protected final OutputStream outputStream;

    public OutputStreamSink(Serializer<?> serializer, OutputStream outputStream) {
        super(serializer);
        this.outputStream = outputStream;
    }

    @Override
    public void init(List<Column<?>> columns) throws IOException {
        this.serializer.init(outputStream, columns);
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
