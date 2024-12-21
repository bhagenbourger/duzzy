package io.duzzy.core.schema;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnType;
import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import io.duzzy.plugin.provider.constant.DoubleConstantProvider;
import java.util.List;

public record SchemaContext(
    InputSchema inputSchema,
    List<Column> columns
) {

  private static final List<Column> DEFAULT_COLUMNS = List.of(
      new Column(
          "BooleanConstantColumn",
          ColumnType.BOOLEAN,
          0f,
          0f,
          List.of(new BooleanConstantProvider(Boolean.TRUE))
      ),
      new Column(
          "DoubleConstantColumn",
          ColumnType.DOUBLE,
          0f,
          0f,
          List.of(new DoubleConstantProvider(1.0d))
      )
  );

  public SchemaContext {
    columns = columns == null || columns.isEmpty() ? DEFAULT_COLUMNS : columns;
  }
}
