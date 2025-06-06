package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XmlSerializer extends Serializer<XmlCustomStreamWriter> {

  private static final String DEFAULT_ROOT_TAG = "rows";
  private static final String DEFAULT_ROW_TAG = "row";

  private final String rootTag;
  private final String rowTag;
  private final XmlMapper mapper;

  @JsonCreator
  public XmlSerializer(
      @JsonProperty("root_tag")
      @JsonAlias({"rootTag", "root-tag"})
      String rootTag,
      @JsonProperty("row_tag")
      @JsonAlias({"rowTag", "row-tag"})
      String rowTag
  ) {
    this.rootTag = rootTag == null ? DEFAULT_ROOT_TAG : rootTag;
    this.rowTag = rowTag == null ? DEFAULT_ROW_TAG : rowTag;
    this.mapper = (XmlMapper) new XmlMapper()
        .registerModule(new JavaTimeModule())
        .setConfig(new XmlMapper().getSerializationConfig().withRootName(this.rowTag));
  }

  @Override
  protected XmlCustomStreamWriter buildWriter(OutputStream outputStream) throws IOException {
    final XMLStreamWriter xmlStreamWriter;
    try {
      xmlStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(outputStream);
      xmlStreamWriter.writeStartDocument();
      xmlStreamWriter.writeStartElement(rootTag);
    } catch (XMLStreamException e) {
      throw new IOException(e);
    }
    return new XmlCustomStreamWriter(xmlStreamWriter);
  }

  @Override
  protected void serialize(DuzzyRow row, XmlCustomStreamWriter xmlStreamWriter)
      throws IOException {
    mapper.writeValue(xmlStreamWriter.xmlStreamWriter(), row.toMap());
  }

  @Override
  public Boolean hasSchema() {
    return false;
  }

  @Override
  public XmlSerializer fork(Long threadId) {
    return new XmlSerializer(rootTag, rowTag);
  }
}
