package io.duzzy.plugin.provider.random;

import static java.time.LocalDateTime.MAX;
import static java.time.LocalDateTime.MIN;
import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.provider.corrupted.LocalDateTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.LocalDateTimeRandomProvider",
    description = "Provide a random local date time value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum local date time, inclusive",
            defaultValue = "1970-01-01T00:00"
        ),
        @Parameter(
            name = "max",
            description = "The maximum local date time, exclusive",
            defaultValue = "2100-01-01T00:00"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.LocalDateTimeRandomProvider"
        min: "2020-01-01T00:00:00"
        max: "2021-01-01T00:00:00"
        """
)
public final class LocalDateTimeRandomProvider implements LocalDateTimeCorruptedProvider {

  private static final Long DEFAULT_MAX = 4102444800L; // 2100-01-01T00:00:00 in seconds since epoch

  private final Long min;
  private final Long max;

  public LocalDateTimeRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public LocalDateTimeRandomProvider(
      @JsonProperty("min") String min,
      @JsonProperty("max") String max
  ) {
    this.min = min == null ? 0 : parse(min, ISO_LOCAL_DATE_TIME).toEpochSecond(UTC);
    this.max = max == null ? DEFAULT_MAX : parse(max, ISO_LOCAL_DATE_TIME).toEpochSecond(UTC);
    assert this.min < this.max : "Min local date time must be before max local date time";
  }

  @Override
  public LocalDateTime value(FieldContext fieldContext) {
    return LocalDateTime.ofEpochSecond(
        fieldContext.random().nextLong(this.min, this.max),
        0,
        UTC
    );
  }
}
