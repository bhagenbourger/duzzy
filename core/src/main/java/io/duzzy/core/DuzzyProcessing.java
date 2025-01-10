package io.duzzy.core;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.provider.ProviderUtil;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import org.apache.commons.codec.digest.MurmurHash3;

public class DuzzyProcessing {

  private final Long start;
  private final Long end;
  private final DuzzySchema duzzySchema;
  private final Sink sink;
  private final Long seed;

  public DuzzyProcessing(Long start, Long end, DuzzySchema duzzySchema, Sink sink, Long seed) {
    this.start = start;
    this.end = end;
    this.duzzySchema = duzzySchema;
    this.sink = sink;
    this.seed = seed;
  }

  public void run() throws Exception {
    sink.init(duzzySchema);
    for (Long index = start; index < end; index++) {
      processRow(index, ProviderUtil.RANDOM_PROVIDERS);
    }
    sink.close();
  }

  private void processRow(
      Long index,
      List<Provider<?>> providers
  ) throws Exception {
    final Long rowId = computeRowId(seed, index);
    final FieldContext fieldContext = new FieldContext(
        providers,
        sink.getSerializer().hasSchema(),
        new Random(rowId),
        rowId,
        index
    );
    sink.write(
        new DataItems(
            duzzySchema
                .fields()
                .stream()
                .map(c -> new DataItem(
                    c.name(),
                    c.type(),
                    c.value(fieldContext)
                ))
                .toList()
        )
    );
  }

  private static Long computeRowId(Long seed, Long index) {
    return MurmurHash3.hash128x64(
        Long.toString(seed ^ index).getBytes(StandardCharsets.UTF_8)
    )[0];
  }
}
