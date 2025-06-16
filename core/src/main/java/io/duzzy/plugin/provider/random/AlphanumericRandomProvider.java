package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.ProviderUtil;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.AlphanumericRandomProvider",
    description = "Provide a random alphanumeric value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min_length",
            aliases = {"minLength", "min-length"},
            description = "The minimum length of the generated value"
        ),
        @Parameter(
            name = "max_length",
            aliases = {"maxLength", "max-length"},
            description = "The maximum length of the generated value"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.AlphanumericRandomProvider"
        min_length: 10
        max_length: 15
        """
)
public class AlphanumericRandomProvider implements StringCorruptedProvider {

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
  public String value(FieldContext fieldContext) {
    int length = fieldContext.random().nextInt(minLength, maxLength + 1);
    return ProviderUtil.randomString(fieldContext.random(), length);
  }

  @Override
  public String corruptedValue(FieldContext fieldContext) {
    return StringCorruptedProvider.corruptedValue(fieldContext, maxLength);
  }
}
