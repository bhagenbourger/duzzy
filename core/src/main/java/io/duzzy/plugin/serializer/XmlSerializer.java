package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;

public class XmlSerializer extends Serializer<XMLCustomStreamWriter> {

    private static final String DEFAULT_ROOT_TAG = "rows";
    private static final String DEFAULT_ROW_TAG = "row";

    private final String rootTag;
    private final XmlMapper mapper;

    @JsonCreator
    public XmlSerializer(
            @JsonProperty("rootTag") String rootTag,
            @JsonProperty("rowTag") String rowTag
    ) {
        this.rootTag = rootTag == null ? DEFAULT_ROOT_TAG : rootTag;
        this.mapper = (XmlMapper) new XmlMapper()
                .registerModule(new JavaTimeModule())
                .setConfig(
                        new XmlMapper()
                                .getSerializationConfig()
                                .withRootName(rowTag == null ? DEFAULT_ROW_TAG : rowTag)
                );
    }

    @Override
    protected XMLCustomStreamWriter buildWriter(OutputStream outputStream) throws IOException {
        final XMLStreamWriter xmlStreamWriter;
        try {
            xmlStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(outputStream);
            xmlStreamWriter.writeStartDocument();
            xmlStreamWriter.writeStartElement(rootTag);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
        return new XMLCustomStreamWriter(xmlStreamWriter);
    }

    @Override
    protected void serialize(DataItems data, XMLCustomStreamWriter xmlStreamWriter) throws IOException {
        mapper.writeValue(xmlStreamWriter.xmlStreamWriter(), data.toMap());
    }

    @Override
    public Boolean hasSchema() {
        return false;
    }
}
