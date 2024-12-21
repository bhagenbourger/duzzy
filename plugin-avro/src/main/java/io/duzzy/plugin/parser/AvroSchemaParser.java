package io.duzzy.plugin.parser;

import static io.duzzy.core.column.ColumnType.DOUBLE;
import static io.duzzy.core.column.ColumnType.DOUBLE_NULLABLE;
import static io.duzzy.core.column.ColumnType.FLOAT;
import static io.duzzy.core.column.ColumnType.FLOAT_NULLABLE;
import static io.duzzy.core.column.ColumnType.INTEGER;
import static io.duzzy.core.column.ColumnType.INTEGER_NULLABLE;
import static io.duzzy.core.column.ColumnType.LONG;
import static io.duzzy.core.column.ColumnType.LONG_NULLABLE;
import static io.duzzy.core.column.ColumnType.STRING;
import static io.duzzy.core.column.ColumnType.STRING_NULLABLE;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.provider.random.BooleanRandomProvider;
import io.duzzy.plugin.provider.random.DoubleRandomProvider;
import io.duzzy.plugin.provider.random.FloatRandomProvider;
import io.duzzy.plugin.provider.random.InstantRandomProvider;
import io.duzzy.plugin.provider.random.IntegerRandomProvider;
import io.duzzy.plugin.provider.random.LocalDateRandomProvider;
import io.duzzy.plugin.provider.random.LongRandomProvider;
import io.duzzy.plugin.provider.random.UuidRandomProvider;
import io.duzzy.plugin.schema.AvroInputSchema;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;

public class AvroSchemaParser implements Parser {

  private static final String NAME = "name";
  private static final String TYPE = "type";

  @Override
  public DuzzyContext parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    return new DuzzyContext(parse(new Schema.Parser().parse(file), duzzyConfig));
  }

  private static SchemaContext parse(Schema avroSchema, DuzzyConfig duzzyConfig) {
    if (avroSchema.hasFields()) {
      final List<Column> columns = avroSchema
          .getFields()
          .stream()
          .map(f -> AvroSchemaParser.parse(f, duzzyConfig))
          .toList();
      return new SchemaContext(new AvroInputSchema(avroSchema), columns);
    }
    return new SchemaContext(new AvroInputSchema(avroSchema), null);
  }

  private static Column parse(Schema.Field field, DuzzyConfig duzzyConfig) {
    if (field.schema().getLogicalType() != null) {
      return parse(field.name(), field.schema().getLogicalType(), duzzyConfig);
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
        throw new RuntimeException(
            "Field name '" + field.name() + "' - union type not nullable is not supported"
        );
      }
    } else {
      return parse(field.name(), field.schema().getType(), field.schema(), duzzyConfig);
    }
  }

  private static Column parse(
      String fieldName,
      LogicalType fieldLogicalType,
      DuzzyConfig duzzyConfig
  ) {
    return switch (fieldLogicalType.getName()) {
      case "uuid" -> getColumn(
          fieldName,
          ColumnType.UUID,
          duzzyConfig,
          new UuidRandomProvider()
      );
      case "date" -> getColumn(
          fieldName,
          ColumnType.DATE,
          duzzyConfig,
          new LocalDateRandomProvider()
      );
      case "decimal" -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro logical type 'decimal' is not supported"
      );
      case "time-millis" -> throw new RuntimeException(
          "Field name '$fieldName' - avro logical type 'time-millis' is not supported"
      );
      case "time-micros" -> throw new RuntimeException(
          "Field name '$fieldName' - avro logical type 'time-micros' is not supported"
      );
      case "timestamp-millis" -> getColumn(
          fieldName,
          ColumnType.TIMESTAMP_MILLIS,
          duzzyConfig,
          new InstantRandomProvider()
      );
      case "timestamp-micros" -> getColumn(
          fieldName,
          ColumnType.TIMESTAMP_MICROS,
          duzzyConfig,
          new InstantRandomProvider()
      );
      case "local-timestamp-millis" -> throw new RuntimeException(
          "Field name '$fieldName' - avro logical type 'local-timestamp-millis' is not supported"
      );
      case "local-timestamp-micros" -> throw new RuntimeException(
          "Field name '$fieldName' - avro logical type 'local-timestamp-micros' is not supported"
      );
      case null, default -> getColumn(
          "test",
          STRING,
          duzzyConfig,
          new AlphanumericRandomProvider()
      ); //TODO:  keep that?
    };
  }

  private static Column parse(
      String fieldName,
      Schema.Type fieldType,
      Schema schema,
      DuzzyConfig duzzyConfig
  ) {
    return switch (fieldType) {
      case Schema.Type.STRING -> getColumn(
          fieldName,
          schema.isNullable() ? STRING_NULLABLE : STRING,
          duzzyConfig,
          new AlphanumericRandomProvider()
      );
      case Schema.Type.INT -> getColumn(
          fieldName,
          schema.isNullable() ? INTEGER_NULLABLE : INTEGER,
          duzzyConfig,
          new IntegerRandomProvider()
      );
      case Schema.Type.LONG -> getColumn(
          fieldName,
          schema.isNullable() ? LONG_NULLABLE : LONG,
          duzzyConfig,
          new LongRandomProvider()
      );
      case Schema.Type.FLOAT -> getColumn(
          fieldName,
          schema.isNullable() ? FLOAT_NULLABLE : FLOAT,
          duzzyConfig,
          new FloatRandomProvider()
      );
      case Schema.Type.DOUBLE -> getColumn(
          fieldName,
          schema.isNullable() ? DOUBLE_NULLABLE : DOUBLE,
          duzzyConfig,
          new DoubleRandomProvider()
      );
      case Schema.Type.BOOLEAN -> getColumn(
          fieldName,
          schema.isNullable() ? ColumnType.BOOLEAN_NULLABLE : ColumnType.BOOLEAN,
          duzzyConfig,
          new BooleanRandomProvider()
      );
      case Schema.Type.ENUM -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'ENUM' is not supported");
      case Schema.Type.UNION -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'UNION' is not supported");
      case Schema.Type.RECORD -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'RECORD' is not supported");
      case Schema.Type.ARRAY -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'ARRAY' is not supported");
      case Schema.Type.MAP -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'MAP' is not supported");
      case Schema.Type.FIXED -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'FIXED' is not supported");
      case Schema.Type.BYTES -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'BYTES' is not supported");
      case Schema.Type.NULL -> throw new RuntimeException(
          "Field name '" + fieldName + "' - avro type 'NULL' is not supported");
    };
  }

  private static Provider<?> getProvider(
      String columnName,
      ColumnType columnType,
      DuzzyConfig duzzyConfig,
      Provider<?> defaultValue
  ) {
    return duzzyConfig != null ? duzzyConfig.findProvider(NAME, columnName)
        .or(() -> duzzyConfig.findProvider(TYPE, columnType.getName()))
        .orElse(defaultValue) : defaultValue;
  }

  private static Column getColumn(
      String columnName,
      ColumnType columnType,
      DuzzyConfig duzzyConfig,
      Provider<?> defaultValue
  ) {
    return new Column(
        columnName,
        columnType,
        null, //TODO null rate
        null, //TODO corrupted rate
        List.of(getProvider(columnName, columnType, duzzyConfig, defaultValue))
    );
  }
}
