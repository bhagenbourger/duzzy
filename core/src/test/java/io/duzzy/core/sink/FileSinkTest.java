package io.duzzy.core.sink;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class FileSinkTest {

  @Test
  void computeFilePart() {
    final String result = FileSink.addFilePart("/tmp/test/toto.txt", 12L);
    assertThat(result).isEqualTo("/tmp/test/toto_12.txt");
  }


  @Test
  void computeFilePartWithoutExtension() {
    final String result = FileSink.addFilePart("/tmp/test/toto", 12L);
    assertThat(result).isEqualTo("/tmp/test/toto_12");
  }
}
