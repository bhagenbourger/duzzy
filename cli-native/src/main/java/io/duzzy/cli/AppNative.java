package io.duzzy.cli;

import io.duzzy.cli.command.DocCommand;
import io.duzzy.cli.command.RunCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = AppInfos.NAME,
    description = AppInfos.DESCRIPTION,
    mixinStandardHelpOptions = true,
    version = AppInfos.VERSION,
    subcommands = {RunCommand.class, DocCommand.class}
)
public class AppNative implements Runnable {

  @Override
  public void run() {
    new CommandLine(new AppNative()).usage(System.out);
  }

  public static void main(String[] args) {
    System.exit(new CommandLine(new AppNative()).execute(args));
  }
}