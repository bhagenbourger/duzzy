package io.duzzy.plugin.serializer;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.parquet.io.OutputFile;
import org.apache.parquet.io.PositionOutputStream;

public class ParquetOutputStreamWriter implements OutputFile {

  private final OutputStream outputStream;

  public ParquetOutputStreamWriter(OutputStream outputStream) {
    this.outputStream = outputStream;
  }

  @Override
  public PositionOutputStream create(long l) throws IOException {
    return createPositionOutputStream();
  }

  @Override
  public PositionOutputStream createOrOverwrite(long l) throws IOException {
    return createPositionOutputStream();
  }

  @Override
  public boolean supportsBlockSize() {
    return false;
  }

  @Override
  public long defaultBlockSize() {
    return 0;
  }

  private PositionOutputStream createPositionOutputStream() {
    return new PositionOutputStream() {

      private Integer pos = 0;

      @Override
      public long getPos() throws IOException {
        return pos;
      }

      @Override
      public void flush() throws IOException {
        super.flush();
        outputStream.flush();
      }

      @Override
      public void close() throws IOException {
        super.close();
        outputStream.close();
      }

      @Override
      public void write(int b) throws IOException {
        outputStream.write(b);
        pos += 1;
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
        pos += len;
      }
    };
  }
}
