package io.duzzy.core.field;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Documentation(
    identifier = "io.duzzy.core.field.Field",
    description =
        "A field representation with a name and a type which manages how the data is generated. "
            + "Field delegates data generation to the provider.",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.FIELD,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "name",
            description = "The field name, must be a string"
        ),
        @Parameter(
            name = "type",
            description = """
                The field value type, the list of supported types:
                BOOLEAN, INTEGER, LONG, FLOAT, DOUBLE, STRING, UUID,
                LOCAL_DATE, INSTANT, TIME_MILLIS, TIME_MICROS, TIMESTAMP_MILLIS, TIMESTAMP_MICROS
                """
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
        - name: stringConstant
          type: STRING
          null_rate: 0.5
          corrupted_rate: 0.5
          providers:
            - identifier: "io.duzzy.plugin.provider.constant.StringConstantProvider"
              value: myConstant
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
