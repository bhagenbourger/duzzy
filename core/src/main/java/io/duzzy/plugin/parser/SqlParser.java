package io.duzzy.plugin.parser;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.provider.random.BooleanRandomProvider;
import io.duzzy.plugin.provider.random.DoubleRandomProvider;
import io.duzzy.plugin.provider.random.FloatRandomProvider;
import io.duzzy.plugin.provider.random.InstantRandomProvider;
import io.duzzy.plugin.provider.random.IntegerRandomProvider;
import io.duzzy.plugin.provider.random.LongRandomProvider;
import io.duzzy.plugin.provider.random.UuidRandomProvider;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.jooq.Meta;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

public class SqlParser implements Parser {

  @Override
  public SchemaContext parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    final Meta meta =
        DSL
            .using(new DefaultConfiguration())
            .meta(Files.readString(file.toPath(), StandardCharsets.UTF_8));

    final List<Field> fields = Arrays
        .stream(meta.getTables().getFirst().fields())
        .map(f -> parse(f, duzzyConfig))
        .toList();

    return new SchemaContext(fields);
  }

  private Field parse(org.jooq.Field<?> jfield, DuzzyConfig duzzyConfig) {
    final TupleProviderType tupleProviderType = getDefaultProvider(jfield);
    return getField(
        jfield.getName(),
        tupleProviderType.type(),
        getNullRate(jfield),
        duzzyConfig,
        tupleProviderType.provider()
    );
  }

  private static TupleProviderType getDefaultProvider(org.jooq.Field<?> jfield) {
    final String typeName = jfield.getType().getName();
    if (String.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.STRING,
          new AlphanumericRandomProvider(1, jfield.getDataType().length())
      );
    }
    if (Boolean.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.BOOLEAN,
          new BooleanRandomProvider()
      );
    }
    if (Integer.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.INTEGER,
          new IntegerRandomProvider()
      );
    }
    if (Long.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.LONG,
          new LongRandomProvider()
      );
    }
    if (Double.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.DOUBLE,
          new DoubleRandomProvider()
      );
    }
    if (Float.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.FLOAT,
          new FloatRandomProvider()
      );
    }
    if (LocalDate.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.LOCAL_DATE,
          new FloatRandomProvider()
      );
    }
    if (UUID.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.UUID,
          new UuidRandomProvider()
      );
    }
    if (Instant.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.INSTANT,
          new InstantRandomProvider()
      );
    }
    if (Timestamp.class.getName().equals(typeName)) {
      return new TupleProviderType(
          Type.TIMESTAMP_MILLIS,
          new InstantRandomProvider()
      );
    }

    throw new UnsupportedOperationException(
        "Field name '" + jfield.getName() + "' - " + typeName + " is not supported"
    );
  }

  private static float getNullRate(org.jooq.Field<?> jfield) {
    return jfield.getDataType().nullable() ? 0.1f : 0f;
  }

  private record TupleProviderType(
      Type type,
      Provider<?> provider
  ) {
  }
}
