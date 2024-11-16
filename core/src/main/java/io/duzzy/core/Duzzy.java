package io.duzzy.core;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.parser.DuzzySchemaParser;
import org.apache.commons.codec.digest.MurmurHash3;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

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

        return generate(getDuzzySchema(getParser(), getDuzzyConfig()));
    }

    private DuzzyConfig getDuzzyConfig() throws IOException {
        return config != null ? DuzzyConfig.fromFile(config) : null;
    }

    private DuzzySchema getDuzzySchema(
            Parser parser,
            DuzzyConfig duzzyConfig
    ) throws IOException {
        return (schema == null ? DuzzySchema.DEFAULT : parser.parse(schema, duzzyConfig))
                .withSeed(this.seed)
                .withRows(this.rows)
                .withSink(duzzyConfig == null ? null : duzzyConfig.sink());
    }

    private Parser getParser()
            throws InstantiationException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException,
            ClassNotFoundException {
        return schemaParser != null && !schemaParser.isEmpty() ?
                Class
                        .forName(schemaParser)
                        .asSubclass(Parser.class)
                        .getDeclaredConstructor()
                        .newInstance() : new DuzzySchemaParser();
    }

    private static DuzzyResult generate(DuzzySchema duzzySchema) throws IOException {
        final Sink sink = duzzySchema.sink();
        sink.init(duzzySchema.columns());
        final Long start = Instant.now().toEpochMilli();
        for (Long index = 0L; index < duzzySchema.rows(); index++) {
            final Long rowId = computeRowId(duzzySchema.seed(), index);
            final ColumnContext columnContext = new ColumnContext(new Random(rowId), rowId, index);
            sink.write(
                    new DataItems(
                            duzzySchema
                                    .columns()
                                    .stream()
                                    .map(c -> new DataItem(
                                            c.getName(),
                                            c.getColumnType(),
                                            c.value(columnContext)
                                    ))
                                    .toList()
                    )
            );
        }
        final Long end = Instant.now().toEpochMilli();
        sink.close();

        return new DuzzyResult(
                Duration.of(end - start, ChronoUnit.MILLIS),
                duzzySchema.rows(),
                duzzySchema.seed()
        );
    }

    private static Long computeRowId(Long seed, Long index) {
        return MurmurHash3.hash128x64(Long.toString(seed ^ index).getBytes())[0];
    }
}
