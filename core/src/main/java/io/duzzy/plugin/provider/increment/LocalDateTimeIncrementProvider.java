package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.LocalDateTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.LocalDateTimeIncrementProvider",
    description = "Provide a local date-time value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to current local date-time",
            defaultValue = "now()"
        ),
        @Parameter(
            name = "step",
            description = "The step value, defaults to 1",
            defaultValue = "1"
        ),
        @Parameter(
            name = "unit",
            description = "The unit of increment (SECONDS, MINUTES, HOURS, DAYS, MONTHS, YEARS), "
                + "defaults to SECONDS",
            defaultValue = "SECONDS"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.LocalDateTimeIncrementProvider"
        start: "2023-01-01T00:00:00"
        step: 1
        unit: DAYS
        """
)
public class LocalDateTimeIncrementProvider implements LocalDateTimeCorruptedProvider {

  private final LocalDateTime start;
  private final Long step;
  private final ChronoUnit unit;

  @JsonCreator
  public LocalDateTimeIncrementProvider(
      @JsonProperty("start") String start,
      @JsonProperty("step") Long step,
      @JsonProperty("unit") String unit
  ) {
    this.start = start == null ? LocalDateTime.now() : LocalDateTime.parse(start);
    this.step = step == null ? Long.valueOf(1) : step;
    this.unit = unit == null ? ChronoUnit.SECONDS :
        ChronoUnit.valueOf(unit.toUpperCase(Locale.getDefault()));
  }

  @Override
  public LocalDateTime value(FieldContext fieldContext) {
    return start.plus(fieldContext.rowIndex() * step, unit);
  }
}
