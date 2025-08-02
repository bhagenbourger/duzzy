package io.duzzy.core.sink;

import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

public abstract class FileSink extends Sink {

  public enum CompressionAlgorithm {
    NONE("none"),
    BZIP2(CompressorStreamFactory.BZIP2),
    GZIP(CompressorStreamFactory.GZIP),
    ZSTD(CompressorStreamFactory.ZSTANDARD);

    private final String name;

    CompressionAlgorithm(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  private final CompressionAlgorithm compressionAlgorithm;

  public FileSink(
      Serializer<?> serializer,
      CompressionAlgorithm compressionAlgorithm) {
    super(serializer);
    this.compressionAlgorithm = compressionAlgorithm == null ? CompressionAlgorithm.NONE :
        compressionAlgorithm;
  }

  @Override
  protected OutputStream outputStreamWrapper(OutputStream outputStream) throws IOException {
    if (compressionAlgorithm == CompressionAlgorithm.NONE) {
      return outputStream;
    }
    return new CompressorStreamFactory()
        .createCompressorOutputStream(compressionAlgorithm.getName(), outputStream);
  }

  protected CompressionAlgorithm getCompressionAlgorithm() {
    return compressionAlgorithm;
  }

  public static String addFilePart(String filename, Long threadId) {
    final int ext = filename.lastIndexOf(".");
    return ext > 0 ? filename.substring(0, ext) + "_" + threadId + filename.substring(ext) :
        filename + "_" + threadId;
  }
}
