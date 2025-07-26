package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.LocalTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalTime;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.LocalTimeConstantProvider",
    description = "Provide a LocalTime constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a LocalTime"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.LocalTimeConstantProvider"
        value: "12:30:00"
        """
)
public class LocalTimeConstantProvider
    extends ConstantProvider<LocalTime>
    implements LocalTimeCorruptedProvider {
  @JsonCreator
  public LocalTimeConstantProvider(
      @JsonProperty("value") LocalTime value
  ) {
    super(value);
  }
}
