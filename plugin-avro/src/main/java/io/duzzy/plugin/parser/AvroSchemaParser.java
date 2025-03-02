package io.duzzy.plugin.parser;

import static io.duzzy.core.field.Type.DOUBLE;
import static io.duzzy.core.field.Type.FLOAT;
import static io.duzzy.core.field.Type.INTEGER;
import static io.duzzy.core.field.Type.LONG;
import static io.duzzy.core.field.Type.STRING;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.provider.random.BooleanRandomProvider;
import io.duzzy.plugin.provider.random.DoubleRandomProvider;
import io.duzzy.plugin.provider.random.FloatRandomProvider;
import io.duzzy.plugin.provider.random.InstantRandomProvider;
import io.duzzy.plugin.provider.random.IntegerRandomProvider;
import io.duzzy.plugin.provider.random.LocalDateRandomProvider;
import io.duzzy.plugin.provider.random.LongRandomProvider;
import io.duzzy.plugin.provider.random.UuidRandomProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;

public class AvroSchemaParser implements Parser {

  @Override
  public DuzzySchema parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    return parse(new Schema.Parser().parse(file), duzzyConfig);
  }

  private DuzzySchema parse(Schema avroSchema, DuzzyConfig duzzyConfig) {
    if (avroSchema.hasFields()) {
      final List<Field> fields = avroSchema
          .getFields()
          .stream()
          .map(f -> parse(f, duzzyConfig))
          .toList();
      return new DuzzySchema(fields);
    }
    return new DuzzySchema(null);
  }

  private Field parse(Schema.Field field, DuzzyConfig duzzyConfig) {
    if (field.schema().getLogicalType() != null) {
      return parse(
          field.name(),
          field.schema().getLogicalType(),
          field.schema(),
          duzzyConfig
      );
    } else if (field.schema().isUnion()) {
      if (field.schema().isNullable()) {
        return parse(
            field.name(),
            field
                .schema()
                .getTypes()
                .stream()
                .filter(t -> t.getType() != Schema.Type.NULL)
                .toList()
                .getFirst()
                .getType(),
            field.schema(),
            duzzyConfig
        );
      } else {
        throw new UnsupportedOperationException(
            "Field name '" + field.name() + "' - union type not nullable is not supported"
        );
      }
    } else {
      return parse(field.name(), field.schema().getType(), field.schema(), duzzyConfig);
    }
  }

  private Field parse(
      String fieldName,
      LogicalType fieldLogicalType,
      Schema schema,
      DuzzyConfig duzzyConfig
  ) {
    return switch (fieldLogicalType.getName()) {
      case "uuid" -> getField(
          fieldName,
          Type.UUID,
          getNullRate(schema),
          duzzyConfig,
          new UuidRandomProvider()
      );
      case "date" -> getField(
          fieldName,
          Type.LOCAL_DATE,
          getNullRate(schema),
          duzzyConfig,
          new LocalDateRandomProvider()
      );
      case "decimal" -> throw new UnsupportedOperationException(
          notSupported(fieldName, "decimal")
      );
      case "time-millis" -> throw new UnsupportedOperationException(
          notSupported(fieldName, "time-millis")
      );
      case "time-micros" -> throw new UnsupportedOperationException(
          notSupported(fieldName, "time-micros")
      );
      case "timestamp-millis" -> getField(
          fieldName,
          Type.TIMESTAMP_MILLIS,
          getNullRate(schema),
          duzzyConfig,
          new InstantRandomProvider()
      );
      case "timestamp-micros" -> getField(
          fieldName,
          Type.TIMESTAMP_MICROS,
          getNullRate(schema),
          duzzyConfig,
          new InstantRandomProvider()
      );
      case "local-timestamp-millis" -> throw new UnsupportedOperationException(
          notSupported(fieldName, "local-timestamp-millis")
      );
      case "local-timestamp-micros" -> throw new UnsupportedOperationException(
          notSupported(fieldName, "local-timestamp-micros")
      );
      case null, default -> throw new UnsupportedOperationException(
          notSupported(fieldName, "null")
      );
    };
  }

  private Field parse(
      String fieldName,
      Schema.Type fieldType,
      Schema schema,
      DuzzyConfig duzzyConfig
  ) {
    return switch (fieldType) {
      case Schema.Type.STRING -> getField(
          fieldName,
          STRING,
          getNullRate(schema),
          duzzyConfig,
          new AlphanumericRandomProvider()
      );
      case Schema.Type.INT -> getField(
          fieldName,
          INTEGER,
          getNullRate(schema),
          duzzyConfig,
          new IntegerRandomProvider()
      );
      case Schema.Type.LONG -> getField(
          fieldName,
          LONG,
          getNullRate(schema),
          duzzyConfig,
          new LongRandomProvider()
      );
      case Schema.Type.FLOAT -> getField(
          fieldName,
          FLOAT,
          getNullRate(schema),
          duzzyConfig,
          new FloatRandomProvider()
      );
      case Schema.Type.DOUBLE -> getField(
          fieldName,
          DOUBLE,
          getNullRate(schema),
          duzzyConfig,
          new DoubleRandomProvider()
      );
      case Schema.Type.BOOLEAN -> getField(
          fieldName,
          Type.BOOLEAN,
          getNullRate(schema),
          duzzyConfig,
          new BooleanRandomProvider()
      );
      case Schema.Type.ENUM -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.ENUM.getName()));
      case Schema.Type.UNION -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.UNION.getName()));
      case Schema.Type.RECORD -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.RECORD.getName()));
      case Schema.Type.ARRAY -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.ARRAY.getName()));
      case Schema.Type.MAP -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.MAP.getName()));
      case Schema.Type.FIXED -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.FIXED.getName()));
      case Schema.Type.BYTES -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.BYTES.getName()));
      case Schema.Type.NULL -> throw new UnsupportedOperationException(
          notSupported(fieldName, Schema.Type.NULL.getName()));
    };
  }

  private static float getNullRate(Schema schema) {
    return schema.isNullable() ? 0.1f : 0f;
  }

  private static String notSupported(String fieldName, String type) {
    return "Field name '" + fieldName + "' - avro type '" + type + "' is not supported";
  }
}
