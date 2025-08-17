package io.duzzy.plugin.provider.increment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.InstantCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Documentation(
    identifier = "io.duzzy.plugin.provider.increment.InstantIncrementProvider",
    description = "Provide an instant value that increments by a step",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "start",
            description = "The starting value, defaults to current instant",
            defaultValue = "now(UTC)"
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
        identifier: "io.duzzy.plugin.provider.increment.InstantIncrementProvider"
        start: "2023-01-01T00:00:00Z"
        step: 1
        unit: DAYS
        """
)
public class InstantIncrementProvider implements InstantCorruptedProvider {

  private final Instant start;
  private final Long step;
  private final ChronoUnit unit;

  @JsonCreator
  public InstantIncrementProvider(
      @JsonProperty("start") String start,
      @JsonProperty("step") Long step,
      @JsonProperty("unit") String unit
  ) {
    this.start = start == null ? Instant.now() : Instant.parse(start);
    this.step = step == null ? Long.valueOf(1) : step;
    this.unit = unit == null ? ChronoUnit.SECONDS :
        ChronoUnit.valueOf(unit.toUpperCase(Locale.getDefault()));
  }

  @Override
  public Instant value(FieldContext fieldContext) {
    return start.plus(fieldContext.rowIndex() * step, unit);
  }
}
