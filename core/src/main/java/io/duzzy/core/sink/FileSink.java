package io.duzzy.core.sink;

public class FileSink {

  public static String addFilePart(String filename, Long threadId) {
    final int ext = filename.lastIndexOf(".");
    return ext > 0 ? filename.substring(0, ext) + "_" + threadId + filename.substring(ext) :
        filename + "_" + threadId;
  }
}
