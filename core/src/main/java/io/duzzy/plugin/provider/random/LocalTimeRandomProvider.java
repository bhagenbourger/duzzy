package io.duzzy.plugin.provider.random;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.LocalTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalTime;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.LocalTimeRandomProvider",
    description = "Provide a random local time value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum local time, inclusive",
            defaultValue = "0"
        ),
        @Parameter(
            name = "max",
            description = "The maximum local time, exclusive",
            defaultValue = "23:59:59"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.LocalTimeRandomProvider"
        min: "10:00:00"
        max: "11:00:00"
        """
)
public final class LocalTimeRandomProvider implements LocalTimeCorruptedProvider {

  private static final int DEFAULT_MAX = 86400; // 23:59:59 in seconds

  private final int min;
  private final int max;

  public LocalTimeRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public LocalTimeRandomProvider(
      @JsonProperty("min") String min,
      @JsonProperty("max") String max
  ) {
    this.min = min == null ? 0 : LocalTime.parse(min, ISO_LOCAL_TIME).toSecondOfDay();
    this.max = max == null ? DEFAULT_MAX : LocalTime.parse(max, ISO_LOCAL_TIME).toSecondOfDay();
    assert this.min < this.max : "Min local time must be before max local time";
  }

  @Override
  public LocalTime value(FieldContext fieldContext) {
    long randomSecondOfDay = fieldContext.random().nextLong(this.min, this.max);
    return LocalTime.ofSecondOfDay(randomSecondOfDay);
  }
}
