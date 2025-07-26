package io.duzzy.plugin.provider.random;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.corrupted.SqlDateCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.sql.Date;
import java.time.LocalDate;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.SqlDateRandomProvider",
    description = "Provide a random java.sql.Date value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "min",
            description = "The minimum date, inclusive",
            defaultValue = "1970-01-01"
        ),
        @Parameter(
            name = "max",
            description = "The maximum date, exclusive",
            defaultValue = "2100-12-31"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.SqlDateRandomProvider"
        min: "2020-01-01"
        max: "2021-01-01"
        """
)
public final class SqlDateRandomProvider implements SqlDateCorruptedProvider {

  private static final Long DEFAULT_MAX = LocalDate.of(2100, 12, 31).toEpochDay();

  private final Long min;
  private final Long max;

  public SqlDateRandomProvider() {
    this(null, null);
  }

  @JsonCreator
  public SqlDateRandomProvider(
      @JsonProperty("min") String min,
      @JsonProperty("max") String max
  ) {
    this.min = min == null ? 0 : LocalDate.parse(min, ISO_LOCAL_DATE).toEpochDay();
    this.max = max == null ? DEFAULT_MAX : LocalDate.parse(max, ISO_LOCAL_DATE).toEpochDay();
    assert this.min < this.max : "Min date must be before max date";
  }

  @Override
  public Date value(FieldContext fieldContext) {
    return Date.valueOf(LocalDate.ofEpochDay(fieldContext.random().nextLong(this.min, this.max)));
  }
}
