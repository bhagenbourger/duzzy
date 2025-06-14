package io.duzzy.plugin.provider.random;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalDate;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.LocalDateRandomProvider",
    description = "Provide a random local date value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum local date, inclusive"
        ),
        @Parameter(
            name = "max",
            description = "The maximum local date, exclusive"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.LocalDateRandomProvider"
        min: "2020-01-01"
        max: "2021-01-01"
        """
)
public final class LocalDateRandomProvider implements Provider<LocalDate> {

  private static final Long DEFAULT_MAX = 2932891L; //9999-12-31 + 1 day

  private final Long min;
  private final Long max;

  public LocalDateRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public LocalDateRandomProvider(
      @JsonProperty("min") String min,
      @JsonProperty("max") String max
  ) {
    this.min = min == null ? 0 : LocalDate.parse(min, ISO_LOCAL_DATE).toEpochDay();
    this.max = max == null ? DEFAULT_MAX : LocalDate.parse(max, ISO_LOCAL_DATE).toEpochDay();
    assert this.min < this.max : "Min local date must be before max local date";
  }

  @Override
  public LocalDate value(FieldContext fieldContext) {
    return LocalDate.ofEpochDay(fieldContext.random().nextLong(this.min, this.max));
  }

  @Override
  public LocalDate corruptedValue(FieldContext fieldContext) {
    return LocalDate.ofEpochDay(fieldContext.random().nextLong(-365243219162L, 365241780471L));
  }
}
