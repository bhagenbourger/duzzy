package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.AvroSerializer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;

public class AvroWithSchemaSerializer extends AvroSerializer<DataFileWriter<GenericData.Record>> {

  @JsonCreator
  public AvroWithSchemaSerializer(
      @JsonProperty("name")
      String name,
      @JsonProperty("namespace")
      String namespace,
      @JsonProperty("schema_file")
      @JsonAlias({"schemaFile", "schema-file"})
      File schemaFile
  ) {
    super(name, namespace, schemaFile);
  }

  @Override
  protected DataFileWriter<GenericData.Record> buildWriter(OutputStream outputStream)
      throws IOException {
    return new DataFileWriter<GenericData.Record>(
        new GenericDatumWriter<>()).create(getSchema(),
        outputStream
    );
  }

  @Override
  protected void serialize(DuzzyRow row, DataFileWriter<GenericData.Record> writer)
      throws IOException {
    writer.append(serializeToRecord(row));
  }

  @Override
  public AvroWithSchemaSerializer fork(Long threadId) {
    return new AvroWithSchemaSerializer(name, namespace, schemaFile);
  }
}
