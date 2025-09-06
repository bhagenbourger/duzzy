package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import java.io.DataOutputStream;
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
  private final Long sizeOfFile;
  private final Long rowsPerFile;

  private DataOutputStream dataOutputStream;

  private long rowCpt = 0;
  private long fileCpt = 0;
  private long totalSize = 0;

  public FileSink(
      Serializer<?> serializer,
      String name,
      CompressionAlgorithm compressionAlgorithm,
      Long sizeOfFile,
      Long rowsPerFile
  ) {
    super(serializer);
    this.name = name;
    this.compressionAlgorithm = compressionAlgorithm == null ? CompressionAlgorithm.NONE :
        compressionAlgorithm;
    this.sizeOfFile = sizeOfFile;
    this.rowsPerFile = rowsPerFile;
  }

  protected abstract OutputStream createOutputStream() throws IOException;

  @Override
  public long size() {
    return totalSize + dataOutputStream.size();
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    this.dataOutputStream = new DataOutputStream(outputStreamCompressor(createOutputStream()));
    getSerializer().init(dataOutputStream, duzzySchema);
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    if ((getSizeOfFile() != null && dataOutputStream.size() >= getSizeOfFile()) || (
        getRowsPerFile() != null && rowCpt >= getRowsPerFile())) {
      rowCpt = 0;
      fileCpt++;
      totalSize += dataOutputStream.size();
      close();
      init(null);
    }
    super.write(row);
    rowCpt++;
  }

  @Override
  public void close() throws Exception {
    super.close();
    dataOutputStream.close();
  }

  private OutputStream outputStreamCompressor(OutputStream outputStream) throws IOException {
    if (compressionAlgorithm == CompressionAlgorithm.NONE) {
      return outputStream;
    }
    return new CompressorStreamFactory()
        .createCompressorOutputStream(compressionAlgorithm.getName(), outputStream);
  }

  protected String forkedName(long id) {
    return computeName(name, id);
  }

  protected String incrementedName() {
    return rowsPerFile == null && sizeOfFile == null ? name : computeName(name, fileCpt);
  }

  protected CompressionAlgorithm getCompressionAlgorithm() {
    return compressionAlgorithm;
  }

  protected Long getSizeOfFile() {
    return sizeOfFile;
  }

  protected Long getRowsPerFile() {
    return rowsPerFile;
  }

  static String computeName(String name, long id) {
    final int ext = name.lastIndexOf(".");
    return ext > 0 ? name.substring(0, ext) + "_" + id + name.substring(ext) :
        name + "_" + id;
  }
}
