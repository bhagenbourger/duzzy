package io.duzzy.core.schema;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import io.duzzy.plugin.provider.constant.DoubleConstantProvider;
import java.util.List;

public record SchemaContext(
    List<Field> fields
) {

  private static final List<Field> DEFAULT_FIELDS = List.of(
      new Field(
          "BooleanConstantField",
          Type.BOOLEAN,
          0f,
          0f,
          List.of(new BooleanConstantProvider(Boolean.TRUE))
      ),
      new Field(
          "DoubleConstantField",
          Type.DOUBLE,
          0f,
          0f,
          List.of(new DoubleConstantProvider(1.0d))
      )
  );

  public SchemaContext {
    fields = fields == null || fields.isEmpty() ? DEFAULT_FIELDS : fields;
  }
}
