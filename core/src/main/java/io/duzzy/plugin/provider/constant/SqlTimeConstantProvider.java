package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.SqlTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.sql.Time;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.SqlTimeConstantProvider",
    description = "Provide a java.sql.Time constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a java.sql.Time"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.SqlTimeConstantProvider"
        value: "12:30:00"
        """
)
public class SqlTimeConstantProvider
    extends ConstantProvider<Time>
    implements SqlTimeCorruptedProvider {
  @JsonCreator
  public SqlTimeConstantProvider(
      @JsonProperty("value") Time value
  ) {
    super(value);
  }
}
