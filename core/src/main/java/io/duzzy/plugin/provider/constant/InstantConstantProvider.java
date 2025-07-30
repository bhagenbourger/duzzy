package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.InstantCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.Instant;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.InstantConstantProvider",
    description = "Provide an Instant constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be an Instant"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.InstantConstantProvider"
        value: "2023-01-01T12:00:00Z"
        """
)
public class InstantConstantProvider
    extends ConstantProvider<Instant>
    implements InstantCorruptedProvider {
  @JsonCreator
  public InstantConstantProvider(
      @JsonProperty("value") Instant value
  ) {
    super(value);
  }
}
