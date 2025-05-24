package io.duzzy.core.serializer;

import static io.duzzy.core.schema.AvroSchemaUtil.buildSchema;
import static io.duzzy.core.schema.AvroSchemaUtil.toPrimitiveType;

import io.duzzy.core.DuzzyRow;
import java.io.File;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

public abstract class AvroSerializer<W extends AutoCloseable> extends OutputStreamSerializer<W> {

  private static final String DEFAULT_NAME = "name";
  private static final String DEFAULT_NAMESPACE = "namespace";

  protected final String name;
  protected final String namespace;
  protected final File schemaFile;
  private Schema schema;

  public AvroSerializer(String name, String namespace, File schemaFile) {
    this.name = name == null ? DEFAULT_NAME : name;
    this.namespace = namespace == null ? DEFAULT_NAMESPACE : namespace;
    this.schemaFile = schemaFile;
  }

  @Override
  public Boolean hasSchema() {
    return true;
  }

  protected GenericData.Record serializeToRecord(DuzzyRow row) {
    final GenericRecordBuilder recordBuilder = new GenericRecordBuilder(getSchema());
    row.cells().forEach(d -> recordBuilder.set(d.name(), toPrimitiveType(d)));
    return recordBuilder.build();
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = buildSchema(schemaFile, namespace, name, getDuzzySchema());
    }
    return schema;
  }
}
