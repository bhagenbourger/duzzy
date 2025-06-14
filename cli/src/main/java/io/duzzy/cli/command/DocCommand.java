package io.duzzy.cli.command;

import io.duzzy.cli.AppInfos;
import io.duzzy.cli.documentation.DocMarkdownFormatter;
import io.duzzy.core.documentation.DuzzyDoc;
import io.duzzy.core.documentation.DuzzyType;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "doc",
    description = "Print Duzzy documentation",
    mixinStandardHelpOptions = true,
    version = AppInfos.VERSION
)
public class DocCommand implements Callable<Integer> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocCommand.class);

  @Option(
      names = {"-t", "--duzzy-type"},
      paramLabel = "DuzzyType",
      description = "Filter documentation by duzzy type"
  )
  DuzzyType duzzyType;

  @Option(
      names = {"-i", "--identifier"},
      paramLabel = "String",
      description = "Filter documentation by identifier name (contains this string)"
  )
  String identifier;

  @Option(
      names = {"-m", "--module"},
      paramLabel = "String",
      description = "Filter documentation by module name (contains this string)"
  )
  String module;

  @Override
  public Integer call() {
    try {
      System.out.println(
          DocMarkdownFormatter.format(DuzzyDoc.generate(duzzyType, identifier, module))
      );
    } catch (Exception e) {
      LOGGER.error("An error occurred while generating Duzzy documentation:", e);
      return 1;
    }
    return 0;
  }
}
