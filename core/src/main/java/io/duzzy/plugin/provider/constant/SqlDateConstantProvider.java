package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.SqlDateCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.sql.Date;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.SqlDateConstantProvider",
    description = "Provide a java.sql.Date constant value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "value",
            description = "The constant value, must be a java.sql.Date"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.constant.SqlDateConstantProvider"
        value: "2023-01-01"
        """
)
public class SqlDateConstantProvider
    extends ConstantProvider<Date>
    implements SqlDateCorruptedProvider {
  @JsonCreator
  public SqlDateConstantProvider(
      @JsonProperty("value") Date value
  ) {
    super(value);
  }
}
