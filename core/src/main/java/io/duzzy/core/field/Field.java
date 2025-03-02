package io.duzzy.core.field;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.provider.Provider;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Documentation(
    identifier = "io.duzzy.core.column.Column",
    description =
        "A column representation with a name and a type which manages how the data is generated. "
            + "Column delegates data generation to the provider.",
    duzzyType = DuzzyType.FIELD,
    parameters = {
        @Parameter(
            name = "name",
            description = "The column name, must be a string"
        ),
        @Parameter(
            name = "column_type",
            aliases = {"columnType", "column-rate"},
            description = "The column value type, must be in //TODO"
        ),
        @Parameter(
            name = "null_rate",
            aliases = {"nullRate", "null-rate"},
            description = "Rate of null values, between 0.0 and 1.0",
            defaultValue = "0.0"
        ),
        @Parameter(
            name = "corrupted_rate",
            aliases = {"corruptedRate", "corrupted-rate"},
            description = "Rate of corrupted values, between 0.0 and 1.0",
            defaultValue = "0.0"
        ),
        @Parameter(
            name = "providers",
            description = "The providers list used to generate the column value"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.column.constant.BooleanConstantColumn"
        null_rate: 0.0
        value: false
        """
)
public record Field(
    @JsonProperty("name")
    String name,
    @JsonProperty("type")
    Type type,
    @JsonProperty("null_rate")
    @JsonAlias({"nullRate", "null-rate"})
    Float nullRate,
    @JsonProperty("corrupted_rate")
    @JsonAlias({"corruptedRate", "corrupted-rate"})
    Float corruptedRate,
    @JsonProperty("providers")
    List<Provider<?>> providers
) {
  private static final Float DEFAULT_RATE = 0f;

  public Field {
    assert name != null && !name.isEmpty() : "Field name can't be null or empty";
    assert type != null : "Field type can't be null";
    assert nullRate == null || (nullRate >= 0 && nullRate <= 1) :
        "Field nullRate must be between 0 and 1";
    assert corruptedRate == null || (corruptedRate >= 0 && corruptedRate <= 1) :
        "Field corruptedRate must be between 0 and 1";
    assert providers != null && !providers.isEmpty() : "Providers can't be null or empty";
    nullRate = nullRate == null ? DEFAULT_RATE : nullRate;
    corruptedRate = corruptedRate == null ? DEFAULT_RATE : corruptedRate;
  }

  public Object value(FieldContext fieldContext) {
    if ((!Objects.equals(corruptedRate(), DEFAULT_RATE))
        && fieldContext.random().nextFloat(0f, 1f) < corruptedRate()) {
      if (fieldContext.hasSchema()) {
        return getProvider(providers(), fieldContext.random()).corruptedValue(fieldContext);
      }
      return getProvider(fieldContext.providers(), fieldContext.random()).value(fieldContext);
    }

    if (!Objects.equals(nullRate(), DEFAULT_RATE)
        && fieldContext.random().nextFloat(0f, 1f) < nullRate()) {
      return null;
    }

    return getProvider(providers(), fieldContext.random()).value(fieldContext);
  }

  public boolean isNullable() {
    return nullRate() > DEFAULT_RATE;
  }

  private static Provider<?> getProvider(
      List<Provider<?>> providers,
      Random random
  ) {
    return providers.get(random.nextInt(0, providers.size()));
  }
}
