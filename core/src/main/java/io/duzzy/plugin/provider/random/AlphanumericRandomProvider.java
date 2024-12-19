package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;

public class AlphanumericRandomProvider implements StringCorruptedProvider {

  private static final int LEFT_LIMIT = 48; // numeral '0'
  private static final int RIGHT_LIMIT = 122; // letter 'z'

  private final int minLength;
  private final int maxLength;

  public AlphanumericRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public AlphanumericRandomProvider(
      @JsonProperty("min_length") @JsonAlias({"minLength", "min-length"}) Integer minLength,
      @JsonProperty("max_length") @JsonAlias({"maxLength", "max-length"}) Integer maxLength
  ) {
    this.minLength = minLength == null ? 10 : minLength;
    this.maxLength = maxLength == null ? 15 : maxLength;
  }

  @Override
  public String value(ColumnContext columnContext) {
    int length = columnContext.random().nextInt(minLength, maxLength + 1);
    return columnContext
        .random()
        .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  @Override
  public String corruptedValue(ColumnContext columnContext) {
    return StringCorruptedProvider.corruptedValue(columnContext, maxLength);
  }
}
