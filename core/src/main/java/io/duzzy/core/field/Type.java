package io.duzzy.core.field;

import java.util.Locale;

public enum Type {
  BOOLEAN,
  INTEGER,
  LONG,
  FLOAT,
  DOUBLE,
  STRING,
  UUID,
  LOCAL_DATE,
  INSTANT,
  TIME_MILLIS,
  TIME_MICROS,
  TIMESTAMP_MILLIS,
  TIMESTAMP_MICROS;

  private final String name;

  Type() {
    this.name = this.name().replace("_", "-").toLowerCase(Locale.ENGLISH);
  }

  public String getName() {
    return this.name;
  }
}
