package io.duzzy.plugin.serializer;

import static io.duzzy.core.schema.AvroSchemaUtil.buildSchema;
import static io.duzzy.core.schema.AvroSchemaUtil.toPrimitiveType;

import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;
import java.io.File;
import java.io.IOException;
import org.apache.iceberg.PartitionSpec;
import org.apache.iceberg.Schema;
import org.apache.iceberg.avro.AvroSchemaUtil;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.parquet.GenericParquetWriter;
import org.apache.iceberg.io.DataWriter;
import org.apache.iceberg.io.OutputFile;
import org.apache.iceberg.parquet.Parquet;

public class IcebergParquetSerializer extends Serializer<DataWriter<GenericRecord>, OutputFile> {

  private static final String DEFAULT_NAME = "name";
  private static final String DEFAULT_NAMESPACE = "namespace";

  protected final String name;
  protected final String namespace;
  protected final File schemaFile;
  private Schema schema;

  public IcebergParquetSerializer(String name, String namespace, File schemaFile) {
    this.name = name == null ? DEFAULT_NAME : name;
    this.namespace = namespace == null ? DEFAULT_NAMESPACE : namespace;
    this.schemaFile = schemaFile;
  }

  @Override
  public long size() {
    return getWriter() == null ? 0 : getWriter().toDataFile().fileSizeInBytes();
  }

  @Override
  public Boolean hasSchema() {
    return null;
  }

  @Override
  protected DataWriter<GenericRecord> buildWriter(OutputFile outputFile) throws IOException {
    return Parquet.writeData(outputFile)
        .schema(getSchema())
        .createWriterFunc(m -> GenericParquetWriter.create(getSchema(), m))
        .overwrite()
        .withSpec(PartitionSpec.unpartitioned())
        .build();
  }

  @Override
  protected void serialize(DataItems data, DataWriter<GenericRecord> writer) {
    final GenericRecord genericRecord = GenericRecord.create(getSchema());
    data.items().forEach(d -> genericRecord.setField(d.name(), toPrimitiveType(d)));
    writer.write(genericRecord);
  }

  @Override
  public Serializer<DataWriter<GenericRecord>, OutputFile> fork(Long threadId) {
    return new IcebergParquetSerializer(name, namespace, schemaFile);
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = AvroSchemaUtil.toIceberg(buildSchema(schemaFile, namespace, name, getDuzzySchema()));
    }
    return schema;
  }
}
