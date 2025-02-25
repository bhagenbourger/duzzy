package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.AvroSerializer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.generic.GenericData;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;

public class ParquetSerializer extends AvroSerializer<ParquetWriter<GenericData.Record>> {

  @JsonCreator
  public ParquetSerializer(
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
  protected ParquetWriter<GenericData.Record> buildWriter(OutputStream outputStream)
      throws IOException {
    return AvroParquetWriter
        .<GenericData.Record>builder(new ParquetOutputStreamWriter(outputStream))
        .withSchema(getSchema())
        .build();
  }

  @Override
  protected void serialize(DataItems data, ParquetWriter<GenericData.Record> writer)
      throws IOException {
    writer.write(serializeToRecord(data));
  }

  @Override
  public ParquetSerializer fork(Long threadId) {
    return new ParquetSerializer(name, namespace, schemaFile);
  }
}
