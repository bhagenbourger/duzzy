package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;

public class AvroWithSchemaSerializer extends AvroSerializer<DataFileWriter<GenericData.Record>> {

  @JsonCreator
  public AvroWithSchemaSerializer(
      @JsonProperty("name") String name,
      @JsonProperty("namespace") String namespace
  ) {
    super(name, namespace);
  }

  @Override
  protected DataFileWriter<GenericData.Record> buildWriter(OutputStream outputStream)
      throws IOException {
    return new DataFileWriter<GenericData.Record>(new GenericDatumWriter<>()).create(getSchema(),
        outputStream);
  }

  @Override
  protected void serialize(DataItems data, DataFileWriter<GenericData.Record> writer)
      throws IOException {
    writer.append(serializeToRecord(data));
  }
}
