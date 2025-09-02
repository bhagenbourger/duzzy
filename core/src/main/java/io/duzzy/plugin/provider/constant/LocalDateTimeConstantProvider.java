package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.LocalDateTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalDateTime;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.LocalDateTimeConstantProvider",
    description = "Provide a LocalDateTime constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a LocalDateTime"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.LocalDateTimeConstantProvider"
        value: "2023-01-01T12:00:00"
        """
)
public class LocalDateTimeConstantProvider
    extends ConstantProvider<LocalDateTime>
    implements LocalDateTimeCorruptedProvider {
  @JsonCreator
  public LocalDateTimeConstantProvider(
      @JsonProperty("value") LocalDateTime value
  ) {
    super(value);
  }
}
