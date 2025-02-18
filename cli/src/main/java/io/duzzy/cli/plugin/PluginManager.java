package io.duzzy.cli.plugin;

import static java.util.stream.Collectors.joining;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PluginManager {

  private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);

  private static final String DUZZY = "DUZZY";
  private static final String IMPLEMENTATION_VENDOR = "Implementation-Vendor";
  private static final String IMPLEMENTATION_TITLE = "Implementation-Title";
  private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
  private static final String DUZZY_PLUGIN_GROUP_ID = "Duzzy-Plugin-Group-Id";
  private static final String DUZZY_PLUGIN_ARTIFACT_ID = "Duzzy-Plugin-Artifact-Id";
  private static final String DUZZY_PLUGIN_DESCRIPTION = "Duzzy-Plugin-Description";

  private final String userHome;

  public PluginManager() {
    this.userHome = System.getProperty("user.home");
  }

  public PluginManager(String userHome) {
    this.userHome = userHome;
  }

  Path getDuzzyPluginsHome() throws IOException {
    final Path path = Paths.get(userHome, ".duzzy", "plugins");
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    return path;
  }

  private Path getPluginJarPath(
      PluginIdentifier pluginIdentifier
  ) throws IOException {
    return Paths.get(
        getDuzzyPluginsHome().toAbsolutePath().toString(),
        pluginIdentifier.jarName()
    );
  }

  private Path getPluginJarPathFromPluginName(String name) throws IOException {
    return Paths.get(
        getDuzzyPluginsHome().toAbsolutePath().toString(),
        name + ".jar"
    );
  }

  public String install(String urlOrPath) throws IOException, URISyntaxException {
    final URL url = safeUrlOrNull(urlOrPath);
    final Path jarPath = url == null ? Path.of(urlOrPath) : download(url);
    final PluginIdentifier pluginIdentifier = getPluginIdentifier(jarPath);
    if (pluginIdentifier.isValid()) {
      final List<PluginIdentifier> installedVersions = checkInstalledVersions(pluginIdentifier);
      if (installedVersions.isEmpty()) {
        Files.copy(
            jarPath,
            getPluginJarPath(pluginIdentifier),
            StandardCopyOption.REPLACE_EXISTING
        );
        return "Plugin " + pluginIdentifier.title() + " is installed.";
      }
      final String uninstallCommands = installedVersions
          .stream()
          .map(PluginIdentifier::qualifiedName)
          .map(n -> "You must uninstall this version before: duzzy plugin uninstall " + n)
          .collect(joining("\n"));
      return "Plugin " + pluginIdentifier.title()
          + " is already installed in versions "
          + installedVersions.stream().map(PluginIdentifier::version).collect(joining(", "))
          + ".\n"
          + uninstallCommands;
    } else {
      return "Specified url or path doesn't refer to a valid plugin.";
    }
  }

  public boolean uninstall(String name) throws IOException {
    return Files.deleteIfExists(getPluginJarPathFromPluginName(name));
  }

  public List<PluginIdentifier> list() throws IOException {
    try (final Stream<Path> plugins = Files.list(getDuzzyPluginsHome())) {
      return plugins.map(PluginManager::getPluginIdentifier).toList();
    }
  }

  private List<PluginIdentifier> checkInstalledVersions(PluginIdentifier pluginIdentifier)
      throws IOException {
    final Map<String, List<PluginIdentifier>> installedPlugins = listByName();
    if (installedPlugins.containsKey(pluginIdentifier.unversionedQualifiedName())) {
      return installedPlugins.get(pluginIdentifier.unversionedQualifiedName());
    }

    return List.of();
  }

  private Map<String, List<PluginIdentifier>> listByName() throws IOException {
    try (final Stream<Path> plugins = Files.list(getDuzzyPluginsHome())) {
      return plugins
          .map(PluginManager::getPluginIdentifier)
          .map(p -> Map.entry(p.unversionedQualifiedName(), p))
          .collect(Collectors.toMap(
              Map.Entry::getKey,
              e -> List.of(e.getValue()),
              (e1, e2) -> Stream.of(e1, e2).flatMap(Collection::stream).toList()
          ));
    }
  }

  private static PluginIdentifier getPluginIdentifier(Path jarPath) {
    try (final JarFile jarFile = new JarFile(jarPath.toFile())) {
      final Manifest manifest = jarFile.getManifest();
      final Attributes mainAttributes = manifest.getMainAttributes();
      return new PluginIdentifier(
          mainAttributes.getValue(IMPLEMENTATION_TITLE),
          mainAttributes.getValue(DUZZY_PLUGIN_DESCRIPTION),
          mainAttributes.getValue(IMPLEMENTATION_VENDOR),
          mainAttributes.getValue(DUZZY_PLUGIN_GROUP_ID),
          mainAttributes.getValue(DUZZY_PLUGIN_ARTIFACT_ID),
          mainAttributes.getValue(IMPLEMENTATION_VERSION),
          jarPath
      );
    } catch (IOException e) {
      logger.warn("Plugin at path {} is invalid", jarPath, e);
      return new PluginIdentifier(jarPath);
    }
  }

  private static Path download(URL url) throws URISyntaxException, IOException {
    final String[] split = url.getPath().split("/");
    final String jarName = split[split.length - 1];
    final String temp = Files.createTempDirectory(DUZZY).toFile().getAbsolutePath();
    final Path tempPath = Paths.get(temp, jarName).toAbsolutePath();
    final ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
    try (final FileOutputStream fileOutputStream = new FileOutputStream(tempPath.toString())) {
      final FileChannel fileChannel = fileOutputStream.getChannel();
      fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
    return tempPath;
  }

  private static URL safeUrlOrNull(String url) {
    try {
      final URI uri = new URI(url);
      if (uri.isAbsolute()) {
        return uri.toURL();
      }
    } catch (MalformedURLException | URISyntaxException e) {
      return null;
    }

    return null;
  }
}
