package io.duzzy.core.engine;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.duzzy.core.DuzzyCell;
import io.duzzy.core.DuzzyLimit;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.ProviderUtil;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import java.time.Instant;
import java.util.Random;
import org.apache.commons.codec.digest.MurmurHash3;

public class DuzzyProcessing {

  private final DuzzySchema duzzySchema;
  private final Sink sink;
  private final Long seed;

  public DuzzyProcessing(
      DuzzySchema duzzySchema,
      Sink sink,
      Long seed
  ) {
    this.duzzySchema = duzzySchema;
    this.sink = sink;
    this.seed = seed;
  }

  public DuzzyEngineResult run(
      int start,
      int step,
      DuzzyLimit duzzyLimit
  ) throws Exception {
    sink.init(duzzySchema);
    long currentRow = 0L;
    long currentDuration = 0L;
    final long startTime = Instant.now().toEpochMilli();
    while (!duzzyLimit.isReached(currentRow, sink.getSerializer().size(), currentDuration)) {
      processRow(start + (step * currentRow));
      currentRow++;
      currentDuration = System.currentTimeMillis() - startTime;
    }
    sink.close();
    return new DuzzyEngineResult(currentRow, sink.getSerializer().size(), currentDuration);
  }

  private void processRow(Long index) throws Exception {
    final Long rowId = computeRowId(seed, index);
    final FieldContext fieldContext = new FieldContext(
        ProviderUtil.RANDOM_PROVIDERS,
        sink.getSerializer().hasSchema(),
        new Random(rowId),
        rowId,
        index
    );
    sink.write(
        new DuzzyRow(
            duzzySchema.rowKey().map(f -> f.value(fieldContext)),
            duzzySchema
                .fields()
                .stream()
                .map(c -> new DuzzyCell(
                    c.name(),
                    c.type(),
                    c.value(fieldContext)
                ))
                .toList()
        )
    );
  }

  private static Long computeRowId(Long seed, Long index) {
    return MurmurHash3.hash128x64(Long.toString(seed ^ index).getBytes(UTF_8))[0];
  }
}
