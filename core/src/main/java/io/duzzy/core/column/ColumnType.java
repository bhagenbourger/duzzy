package io.duzzy.core.column;

import java.util.Locale;

public enum ColumnType {
  RECORD,
  ENUM,
  ARRAY,
  MAP,
  UNION,
  FIXED,
  BYTES,
  STRING,
  STRING_NULLABLE,
  INTEGER,
  INTEGER_NULLABLE,
  LONG,
  LONG_NULLABLE,
  FLOAT,
  FLOAT_NULLABLE,
  DOUBLE,
  DOUBLE_NULLABLE,
  BOOLEAN,
  BOOLEAN_NULLABLE,
  NULL,
  UUID,
  DATE,
  DECIMAL,
  TIME_MILLIS,
  TIME_MICROS,
  TIMESTAMP_MILLIS,
  TIMESTAMP_MICROS,
  LOCAL_TIMESTAMP_MILLIS,
  LOCAL_TIMESTAMP_MICROS;

  private final String name;

  ColumnType() {
    this.name = this.name().replace("_", "-").toLowerCase(Locale.ENGLISH);
  }

  public String getName() {
    return this.name;
  }
}
