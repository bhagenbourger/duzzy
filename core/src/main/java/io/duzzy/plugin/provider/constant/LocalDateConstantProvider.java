package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.LocalDateCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalDate;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.LocalDateConstantProvider",
    description = "Provide a LocalDate constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a LocalDate"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.LocalDateConstantProvider"
        value: "2023-01-01"
        """
)
public class LocalDateConstantProvider
    extends ConstantProvider<LocalDate>
    implements LocalDateCorruptedProvider {
  @JsonCreator
  public LocalDateConstantProvider(
      @JsonProperty("value") LocalDate value
  ) {
    super(value);
  }
}
