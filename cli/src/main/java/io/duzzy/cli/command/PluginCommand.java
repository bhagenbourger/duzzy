package io.duzzy.cli.command;

import io.duzzy.cli.AppInfos;
import io.duzzy.cli.plugin.PluginIdentifier;
import io.duzzy.cli.plugin.PluginManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "plugin",
    description = "Manage your plugins",
    mixinStandardHelpOptions = true,
    version = AppInfos.VERSION
)
public class PluginCommand implements Callable<Integer> {

  private static final Logger logger = LoggerFactory.getLogger(PluginCommand.class);

  private final PluginManager pluginManager;

  public PluginCommand() {
    pluginManager = new PluginManager();
  }

  PluginCommand(String home) {
    this.pluginManager = new PluginManager(home);
  }

  @Override
  public Integer call() {
    new CommandLine(new PluginCommand()).usage(System.out);
    return 0;
  }

  @Command(
      name = "install",
      description = "Install a plugin",
      mixinStandardHelpOptions = true,
      version = AppInfos.VERSION
  )
  int install(
      @Option(
          names = {"-s", "--source"},
          paramLabel = "String",
          description = "Url or local path to the plugin",
          required = true
      ) String source) {
    try {
      System.out.println(pluginManager.install(source));
    } catch (IOException | URISyntaxException e) {
      logger.error("Error while installing plugin {}", source, e);
      System.out.println("Error while installing plugin " + source);
      return 1;
    }
    return 0;
  }

  @Command(
      name = "uninstall",
      description = "Uninstall a plugin",
      mixinStandardHelpOptions = true,
      version = AppInfos.VERSION
  )
  int uninstall(
      @Parameters(
          index = "0",
          description = "Qualified name of the plugin to uninstall"
      )
      String qualifiedName
  ) {
    try {
      if (pluginManager.uninstall(qualifiedName)) {
        System.out.println("Plugin " + qualifiedName + " successfully deleted.");
      } else {
        System.out.println("Plugin " + qualifiedName + " is not installed.");
      }
    } catch (IOException e) {
      logger.error("Error while uninstalling plugin {}", qualifiedName, e);
      System.out.println("Error while uninstalling plugin " + qualifiedName);
      return 1;
    }

    return 0;
  }

  @Command(
      name = "list",
      description = "List all installed plugins",
      mixinStandardHelpOptions = true,
      version = AppInfos.VERSION
  )
  int list() {
    try {
      System.out.println("Installed plugins:");
      pluginManager
          .list()
          .stream()
          .sorted(Comparator.comparing(PluginIdentifier::printTitle)
              .thenComparing(PluginIdentifier::qualifiedName))
          .map(PluginIdentifier::print)
          .forEach(System.out::println);
    } catch (IOException e) {
      logger.error("Error while listing installed plugins", e);
      System.out.println("Impossible to list installed plugins");
      return 1;
    }
    return 0;
  }
}
