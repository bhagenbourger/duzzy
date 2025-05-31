package io.duzzy.core.schema;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.provider.random.BooleanRandomProvider;
import io.duzzy.plugin.provider.random.DoubleRandomProvider;
import java.util.List;
import java.util.Optional;

public record DuzzySchema(
    Optional<Field> rowKey,
    List<Field> fields
) {

  private static final List<Field> DEFAULT_FIELDS = List.of(
      new Field(
          "BooleanRandomProvider",
          Type.BOOLEAN,
          0f,
          0f,
          List.of(new BooleanRandomProvider())
      ),
      new Field(
          "AlphanumericRandomProvider",
          Type.STRING,
          0f,
          0f,
          List.of(new AlphanumericRandomProvider())
      ),
      new Field(
          "DoubleRandomProvider",
          Type.DOUBLE,
          0f,
          0f,
          List.of(new DoubleRandomProvider())
      )
  );

  public DuzzySchema {
    fields = fields == null || fields.isEmpty() ? DEFAULT_FIELDS : fields;
  }
}
