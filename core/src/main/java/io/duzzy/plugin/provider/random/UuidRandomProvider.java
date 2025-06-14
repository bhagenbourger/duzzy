package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.UuidRandomProvider",
    description = "Provide a random UUID value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.UuidRandomProvider"
        """
)
public class UuidRandomProvider implements Provider<UUID> {

  private static final int LEFT_LIMIT = 48; // numeral '0'
  private static final int RIGHT_LIMIT = 122; // letter 'z'
  private static final int LENGTH = 20;

  @JsonCreator
  public UuidRandomProvider() {
  }

  @Override
  public UUID value(FieldContext fieldContext) {
    final String value = fieldContext
        .random()
        .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
        .limit(LENGTH)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
    return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public UUID corruptedValue(FieldContext fieldContext) {
    return value(fieldContext);
  }
}
