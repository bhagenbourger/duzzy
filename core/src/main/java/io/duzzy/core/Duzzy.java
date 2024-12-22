package io.duzzy.core;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.reflection.ReflectionUtility;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.parser.DuzzySchemaParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import org.apache.commons.codec.digest.MurmurHash3;

public class Duzzy {

  private final File schema;
  private final File config;
  private final Long seed;
  private final Long rows;
  private final String schemaParser;

  public Duzzy(File schema, File config, Long seed, Long rows, String schemaParser) {
    this.schema = schema;
    this.config = config;
    this.seed = seed;
    this.rows = rows;
    this.schemaParser = schemaParser;
  }

  public DuzzyResult generate()
      throws IOException,
      ClassNotFoundException,
      NoSuchMethodException,
      InvocationTargetException,
      InstantiationException,
      IllegalAccessException {

    //TODO: Log a WARN if parser is instance of DuzzySchemaParser.class and DuzzyConfig is not null
    // => DuzzyConf is useless with DuzzySchemaParser

    return generate(createDuzzyContext(loadParser(), loadDuzzyConfig()));
  }

  private static DuzzyResult generate(DuzzyContext duzzyContext) throws IOException {
    final Long start = Instant.now().toEpochMilli();
    final Sink sink = duzzyContext.sink();
    sink.init(duzzyContext.schemaContext());
    final List<Provider<?>> providers = ReflectionUtility.loadDuzzyProviders();
    for (Long index = 0L; index < duzzyContext.rows(); index++) {
      processRow(duzzyContext, index, providers, sink);
    }
    sink.close();
    final Long end = Instant.now().toEpochMilli();

    return new DuzzyResult(
        Duration.of(end - start, ChronoUnit.MILLIS),
        duzzyContext.rows(),
        duzzyContext.seed()
    );
  }

  private DuzzyConfig loadDuzzyConfig() throws IOException {
    return config != null ? DuzzyConfig.fromFile(config) : null;
  }

  private DuzzyContext createDuzzyContext(
      Parser parser,
      DuzzyConfig duzzyConfig
  ) throws IOException {
    return (schema == null ? DuzzyContext.DEFAULT : parser.parse(schema, duzzyConfig))
        .withSeed(this.seed)
        .withRows(this.rows)
        .withSink(duzzyConfig == null ? null : duzzyConfig.sink());
  }

  private Parser loadParser()
      throws InstantiationException,
      IllegalAccessException,
      InvocationTargetException,
      NoSuchMethodException,
      ClassNotFoundException {
    return schemaParser != null && !schemaParser.isEmpty() ? Class
        .forName(schemaParser)
        .asSubclass(Parser.class)
        .getDeclaredConstructor()
        .newInstance() : new DuzzySchemaParser();
  }

  private static void processRow(
      DuzzyContext duzzyContext,
      Long index,
      List<Provider<?>> providers,
      Sink sink
  ) throws IOException {
    final Long rowId = computeRowId(duzzyContext.seed(), index);
    final ColumnContext columnContext = new ColumnContext(
        providers,
        sink.getSerializer().hasSchema(),
        new Random(rowId),
        rowId,
        index
    );
    sink.write(
        new DataItems(
            duzzyContext
                .schemaContext()
                .columns()
                .stream()
                .map(c -> new DataItem(
                    c.name(),
                    c.columnType(),
                    c.value(columnContext)
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
