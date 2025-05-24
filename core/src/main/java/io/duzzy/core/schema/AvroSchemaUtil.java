package io.duzzy.core.schema;

import io.duzzy.core.DuzzyCell;
import io.duzzy.core.field.Field;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AvroSchemaUtil {

  private static final Logger logger = LoggerFactory.getLogger(AvroSchemaUtil.class);

  private AvroSchemaUtil() {
  }

  public static Object toPrimitiveType(DuzzyCell cell) {
    return switch (cell.type()) {
      case UUID -> cell.value().toString();
      case LOCAL_DATE -> ((LocalDate) cell.value()).toEpochDay();
      case TIMESTAMP_MILLIS -> ((Instant) cell.value()).toEpochMilli();
      case TIMESTAMP_MICROS -> ChronoUnit.MICROS.between(Instant.EPOCH, ((Instant) cell.value()));
      default -> cell.value();
    };
  }

  public static Schema buildSchema(
      File schemaFile,
      String namespace,
      String name,
      DuzzySchema duzzySchema
  ) {
    Schema innerSchema = null;
    try {
      innerSchema = fromFile(schemaFile);
    } catch (IOException e) {
      logger.warn(
          "Error while building schema from file, fallback on creating schema from fields",
          e
      );
    }
    return innerSchema == null ? fromFields(namespace, name, duzzySchema) : innerSchema;
  }

  private static Schema fromFile(File schemaFile) throws IOException {
    return schemaFile == null ? null : new Schema.Parser().parse(schemaFile);
  }

  private static Schema fromFields(String namespace, String name,
                                   DuzzySchema duzzySchema) {
    final SchemaBuilder.FieldAssembler<Schema> fields = SchemaBuilder
        .record(name)
        .namespace(namespace)
        .fields();
    duzzySchema.fields().forEach(f -> {
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

  private static SchemaBuilder.BaseFieldTypeBuilder<Schema> createAvroField(
      SchemaBuilder.FieldAssembler<Schema> fields,
      Field field
  ) {
    SchemaBuilder.FieldTypeBuilder<Schema> fieldTypeBuilder = fields.name(field.name()).type();
    return field.isNullable() ? fieldTypeBuilder.nullable() : fieldTypeBuilder;
  }
}
