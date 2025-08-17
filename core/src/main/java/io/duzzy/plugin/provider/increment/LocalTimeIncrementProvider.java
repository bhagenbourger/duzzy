package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.LocalTimeCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.LocalTimeIncrementProvider",
    description = "Provide a local time value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to current local time",
            defaultValue = "now()"
        ),
        @Parameter(
            name = "step",
            description = "The step value, defaults to 1",
            defaultValue = "1"
        ),
        @Parameter(
            name = "unit",
            description = "The unit of increment (SECONDS, MINUTES, HOURS), defaults to SECONDS",
            defaultValue = "SECONDS"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.increment.LocalTimeIncrementProvider"
        start: "00:00:00"
        step: 1
        unit: HOURS
        """
)
public class LocalTimeIncrementProvider implements LocalTimeCorruptedProvider {

  private final LocalTime start;
  private final Long step;
  private final ChronoUnit unit;

  @JsonCreator
  public LocalTimeIncrementProvider(
      @JsonProperty("start") String start,
      @JsonProperty("step") Long step,
      @JsonProperty("unit") String unit
  ) {
    this.start = start == null ? LocalTime.now() : LocalTime.parse(start);
    this.step = step == null ? Long.valueOf(1) : step;
    this.unit = unit == null ? ChronoUnit.SECONDS :
        ChronoUnit.valueOf(unit.toUpperCase(Locale.getDefault()));
  }

  @Override
  public LocalTime value(FieldContext fieldContext) {
    return start.plus(fieldContext.rowIndex() * step, unit);
  }
}
