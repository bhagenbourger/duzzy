package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
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

  private final String name;
  private final CompressionAlgorithm compressionAlgorithm;
  private final Long size;
  private final Long rows;

  private long currentSize = 0;
  private long currentRows = 0;

  public FileSink(
      Serializer<?> serializer,
      String name,
      CompressionAlgorithm compressionAlgorithm,
      Long size,
      Long rows
  ) {
    super(serializer);
    this.name = name;
    this.compressionAlgorithm = compressionAlgorithm == null ? CompressionAlgorithm.NONE :
        compressionAlgorithm;
    this.size = size;
    this.rows = rows;
  }

  @Override
  protected OutputStream outputStreamWrapper(OutputStream outputStream) throws IOException {
    if (compressionAlgorithm == CompressionAlgorithm.NONE) {
      return outputStream;
    }
    return new CompressorStreamFactory()
        .createCompressorOutputStream(compressionAlgorithm.getName(), outputStream);
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    super.write(row);
    currentRows++;
    if ((size != null && getSerializer().size() >= size) || (rows != null && currentRows >= rows)) {
      currentRows = 0;
      currentSize += getSerializer().size();
      close();
      resetOutputStream();
      getSerializer().reset();
    }
  }

  @Override
  public long size() {
    return super.size() + currentSize;
  }

  protected String getName() {
    return name;
  }

  protected CompressionAlgorithm getCompressionAlgorithm() {
    return compressionAlgorithm;
  }

  protected Long getSize() {
    return size;
  }

  protected Long getRows() {
    return rows;
  }

  public static String addFilePart(String filename, Long threadId) {
    final int ext = filename.lastIndexOf(".");
    return ext > 0 ? filename.substring(0, ext) + "_" + threadId + filename.substring(ext) :
        filename + "_" + threadId;
  }
}
