package io.duzzy.plugin.serializer;

import static io.duzzy.core.schema.AvroSchemaUtil.buildSchema;
import static io.duzzy.core.schema.AvroSchemaUtil.toPrimitiveType;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.Serializer;
import java.io.File;
import java.io.IOException;
import org.apache.iceberg.DataFile;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.avro.AvroSchemaUtil;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.parquet.GenericParquetWriter;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.io.OutputFile;
import org.apache.iceberg.parquet.Parquet;

public class IcebergParquetSerializer extends Serializer<DataWriter<GenericRecord>, OutputFile> {

  private static final String DEFAULT_SCHEMA_NAME = "name";
  private static final String DEFAULT_SCHEMA_NAMESPACE = "namespace";

  protected final String schemaName;
  protected final String schemaNamespace;
  protected final File schemaFile;
  private Schema schema;

  @JsonCreator
  public IcebergParquetSerializer(
      @JsonProperty("schema_name")
      @JsonAlias({"schemaName", "schema-name"})
      String schemaName,
      @JsonProperty("schema_namespace")
      @JsonAlias({"schemaNamespace", "schema-namespace"})
      String schemaNamespace,
      @JsonProperty("schema_file")
      @JsonAlias({"schemaFile", "schema-file"})
      File schemaFile
  ) {
    this.schemaName = schemaName == null ? DEFAULT_SCHEMA_NAME : schemaName;
    this.schemaNamespace = schemaNamespace == null ? DEFAULT_SCHEMA_NAMESPACE : schemaNamespace;
    this.schemaFile = schemaFile;
  }

  @Override
  protected long size(OutputFile output, DataWriter<GenericRecord> writer) {
    return writer == null ? 0 : writer.toDataFile().fileSizeInBytes();
  }

  @Override
  public Boolean hasSchema() {
    return true;
  }

  @Override
  protected DataWriter<GenericRecord> buildWriter(OutputFile outputFile) throws IOException {
    return Parquet.writeData(outputFile)
        .schema(getSchema())
        .createWriterFunc(m -> GenericParquetWriter.create(getSchema(), m))
        .overwrite(false)
        .withSpec(PartitionSpec.unpartitioned())
        .build();
  }

  @Override
  protected void serialize(DuzzyRow row, DataWriter<GenericRecord> writer) {
    final GenericRecord genericRecord = GenericRecord.create(getSchema());
    row.cells().forEach(c -> genericRecord.setField(c.name(), toPrimitiveType(c)));
    writer.write(genericRecord);
  }

  @Override
  public Serializer<DataWriter<GenericRecord>, OutputFile> fork(Long threadId) {
    return new IcebergParquetSerializer(schemaName, schemaNamespace, schemaFile);
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = AvroSchemaUtil.toIceberg(
          buildSchema(schemaFile, schemaNamespace, schemaName, getDuzzySchema()));
    }
    return schema;
  }

  public DataFile getDataFile() {
    return getWriter().toDataFile();
  }
}
