package io.duzzy.core.field;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.Provider;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
