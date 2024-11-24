package io.duzzy.cli;

import io.duzzy.cli.doc.DocMarkdownFormatter;
import io.duzzy.cli.output.OutputFormat;
import io.duzzy.core.Duzzy;
import io.duzzy.core.DuzzyResult;
import io.duzzy.core.documentation.DuzzyDoc;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

@Command(name = "duzzy", mixinStandardHelpOptions = true, version = "1.0")
public class App implements Callable<Integer> {

    @Option(
            names = {"-f", "--schema-file"},
            paramLabel = "File",
            description = "Schema source file"
    )
    File schema;

    @Option(
            names = {"-c", "--config-file"},
            paramLabel = "File",
            description = "Config file used to enrich the schema"
    )
    File config;

    @Option(
            names = {"-p", "--schema-parser"},
            paramLabel = "Class",
            description = "Qualified name of the parser class used to parse schema file"
    )
    String parser;

    @Option(
            names = {"-s", "--seed"},
            paramLabel = "Long",
            description = "Seed used to generate"
    )
    Long seed;

    @Option(
            names = {"-r", "--rows"},
            paramLabel = "Long",
            description = "Number of rows to generate"
    )
    Long rows;

    @Option(
            names = {"-o", "--output"},
            paramLabel = "OutputFormat",
            description = "Output format, supported values: ${COMPLETION-CANDIDATES}"
    )
    OutputFormat outputFormat = OutputFormat.RAW;

    @Command(name = "doc", description = "Print Duzzy documentation")
    void subCommandDocMethod() throws IOException {
        System.out.println(DocMarkdownFormatter.format(new DuzzyDoc().generate()));
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new App()).execute(args));
    }

    @Override
    public Integer call() throws Exception {
        final DuzzyResult duzzyResult = new Duzzy(schema, config, seed, rows, parser).generate();
        System.out.println();
        System.out.println();
        System.out.println(outputFormat.getDuzzyResultVisitor().format(duzzyResult));
        return 0;
    }
}