package io.duzzy.core.serializer;

import io.duzzy.core.DataItems;
import io.duzzy.core.DuzzyContext;
import io.duzzy.core.Plugin;
import io.duzzy.core.column.Column;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class Serializer<W extends Closeable> implements Plugin {

    private W writer;
    private DuzzyContext duzzyContext;

    protected abstract W buildWriter(OutputStream outputStream) throws IOException;

    protected abstract void write(DataItems data, W writer) throws IOException;

    public void init(OutputStream outputStream, DuzzyContext duzzyContext) throws IOException {
        this.duzzyContext = duzzyContext;
        this.writer = buildWriter(outputStream);
    }

    public void writeAll(DataItems data) throws IOException {
        if (writer == null) {
            throw new RuntimeException("writer must be initiated - call init(OutputStream outputStream) method");
        }
        write(data, writer);
    }

    public void writeUnit(DataItems data, OutputStream outputStream) throws IOException {
        W localWriter = buildWriter(outputStream);
        write(data, localWriter);
        localWriter.close();
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    protected DuzzyContext getDuzzyContext() {
        return duzzyContext;
    }
}
