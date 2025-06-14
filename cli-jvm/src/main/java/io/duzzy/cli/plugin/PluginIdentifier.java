package io.duzzy.cli.plugin;

import java.nio.file.Path;

public record PluginIdentifier(
    String title,
    String description,
    String vendor,
    String groupId,
    String artifactId,
    String version,
    Path path
) {

  public PluginIdentifier(Path path) {
    this(null, null, null, null, null, null, path);
  }

  public String qualifiedName() {
    if (groupId() == null || artifactId() == null || version() == null) {
      return getFilename();
    } else {
      return groupId() + "_" + artifactId() + "_" + version();
    }
  }

  public String unversionedQualifiedName() {
    if (groupId() == null || artifactId() == null) {
      return getFilename();
    } else {
      return groupId() + "_" + artifactId();
    }
  }

  public String jarName() {
    return qualifiedName() + ".jar";
  }

  public String print() {
    return "- " + printTitle()
        + "\n\tqualified name: " + replaceNull(qualifiedName())
        + "\n\tdescription: " + replaceNull(description())
        + "\n\tvendor: " + replaceNull(vendor())
        + "\n\tgroupId: " + replaceNull(groupId())
        + "\n\tartifactId: " + replaceNull(artifactId())
        + "\n\tversion: " + replaceNull(version())
        + "\n\tjar: " + path().toAbsolutePath();
  }

  public String printTitle() {
    return replaceNull(title());
  }

  public boolean isValid() {
    return !(title() == null
        || description() == null
        || vendor() == null
        || groupId() == null
        || artifactId() == null
        || version() == null);
  }

  private String getFilename() {
    final Path fileName = path.getFileName();
    return fileName == null ? null : fileName.toString().split("\\.")[0];
  }

  private static String replaceNull(String value) {
    return value == null ? "Unknown" : value;
  }
}
