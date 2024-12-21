package io.duzzy.core;

public interface Plugin {

  default String getIdentifier() {
    return this.getClass().getCanonicalName();
  }
}
