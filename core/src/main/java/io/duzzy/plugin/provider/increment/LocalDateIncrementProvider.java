package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.LocalDateCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.LocalDateIncrementProvider",
    description = "Provide a local date value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to current date",
            defaultValue = "now(UTC)"
        ),
        @Parameter(
            name = "step",
            description = "The step value, defaults to 1",
            defaultValue = "1"
        ),
        @Parameter(
            name = "unit",
            description = "The unit of increment (DAYS, WEEKS, MONTHS, YEARS), defaults to DAYS",
            defaultValue = "DAYS"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.LocalDateIncrementProvider"
        start: "2023-01-01"
        step: 1
        unit: DAYS
        """
)
public class LocalDateIncrementProvider implements LocalDateCorruptedProvider {

  private final LocalDate start;
  private final Long step;
  private final ChronoUnit unit;

  @JsonCreator
  public LocalDateIncrementProvider(
      @JsonProperty("start") String start,
      @JsonProperty("step") Long step,
      @JsonProperty("unit") String unit
  ) {
    this.start = start == null ? LocalDate.now() : LocalDate.parse(start);
    this.step = step == null ? Long.valueOf(1) : step;
    this.unit = unit == null ? ChronoUnit.DAYS :
        ChronoUnit.valueOf(unit.toUpperCase(Locale.getDefault()));
  }

  @Override
  public LocalDate value(FieldContext fieldContext) {
    return start.plus(fieldContext.rowIndex() * step, unit);
  }
}
