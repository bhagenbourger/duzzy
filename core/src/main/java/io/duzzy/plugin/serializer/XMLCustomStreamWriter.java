package io.duzzy.plugin.serializer;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Closeable;
import java.io.IOException;

public record XMLCustomStreamWriter(XMLStreamWriter xmlStreamWriter) implements Closeable {

    @Override
    public void close() throws IOException {
        try {
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }
}
