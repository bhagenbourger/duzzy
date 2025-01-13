package io.duzzy.core.serializer;

import io.duzzy.core.DataItem;
import io.duzzy.core.DataItems;
import io.duzzy.core.field.Field;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AvroSerializer<W extends Closeable> extends Serializer<W> {

  private static final Logger logger = LoggerFactory.getLogger(AvroSerializer.class);

  private static final String DEFAULT_NAME = "name";
  private static final String DEFAULT_NAMESPACE = "namespace";

  private final String name;
  private final String namespace;
  private final File schemaFile;
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

  private Schema buildSchemaFromFile() throws IOException {
    return schemaFile == null ? null : new Schema.Parser().parse(schemaFile);
  }

  private Schema buildSchemaFromFields() {
    final SchemaBuilder.FieldAssembler<Schema> fields = SchemaBuilder
        .record(name)
        .namespace(namespace)
        .fields();
    getSchemaContext().fields().forEach(f -> {
      switch (f.type()) {
        case INTEGER -> createAvroField(fields, f).intType().noDefault();
        case LONG -> createAvroField(fields, f).longType().noDefault();
        case FLOAT -> createAvroField(fields, f).floatType().noDefault();
        case DOUBLE -> createAvroField(fields, f).doubleType().noDefault();
        case BOOLEAN -> createAvroField(fields, f).booleanType().noDefault();
        case STRING -> createAvroField(fields, f).stringType().noDefault();
        case UUID -> fields
            .name(f.name())
            .type(LogicalTypes.uuid().addToSchema(Schema.create(Schema.Type.STRING)))
            .noDefault();
        case LOCAL_DATE -> fields
            .name(f.name())
            .type(LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)))
            .noDefault();
        case TIMESTAMP_MILLIS -> fields
            .name(f.name())
            .type(LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)))
            .noDefault();
        case TIMESTAMP_MICROS -> fields
            .name(f.name())
            .type(LogicalTypes.timestampMicros().addToSchema(Schema.create(Schema.Type.LONG)))
            .noDefault();
        default -> throw new UnsupportedOperationException(
            "Field type " + f.type() + " is not supported"
        );
      }
    });
    return fields.endRecord();
  }

  private Schema buildSchema() {
    Schema innerSchema = null;
    try {
      innerSchema = buildSchemaFromFile();
    } catch (IOException e) {
      logger.warn(
          "Error while building schema from file, fallback on creating schema from fields",
          e
      );
    }
    return innerSchema == null ? buildSchemaFromFields() : innerSchema;
  }

  protected GenericData.Record serializeToRecord(DataItems data) {
    final GenericRecordBuilder recordBuilder = new GenericRecordBuilder(getSchema());
    data.items().forEach(d -> recordBuilder.set(d.name(), toPrimitiveType(d)));
    return recordBuilder.build();
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = buildSchema();
    }
    return schema;
  }

  private static Object toPrimitiveType(DataItem item) {
    return switch (item.type()) {
      case UUID -> item.value().toString();
      case LOCAL_DATE -> ((LocalDate) item.value()).toEpochDay();
      case TIMESTAMP_MILLIS -> ((Instant) item.value()).toEpochMilli();
      case TIMESTAMP_MICROS -> ChronoUnit.MICROS.between(Instant.EPOCH, ((Instant) item.value()));
      default -> item.value();
    };
  }

  private static SchemaBuilder.BaseFieldTypeBuilder<Schema> createAvroField(
      SchemaBuilder.FieldAssembler<Schema> fields,
      Field field
  ) {
    SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder = fields.name(field.name()).type();
    return field.isNullable() ? fieldTypeBuilder.nullable() : fieldTypeBuilder;
  }
}
