package io.duzzy.plugin.serializer;

import java.io.Closeable;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public record XmlCustomStreamWriter(XMLStreamWriter xmlStreamWriter) implements Closeable {

  @Override
  public void close() throws IOException {
    try {
      xmlStreamWriter.close();
    } catch (XMLStreamException e) {
      throw new IOException(e);
    }
  }
}
