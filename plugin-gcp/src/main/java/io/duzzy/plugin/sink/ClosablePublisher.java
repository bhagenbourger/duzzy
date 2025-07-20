package io.duzzy.plugin.sink;

import com.google.cloud.pubsub.v1.Publisher;
import java.io.Closeable;
import java.io.IOException;

public class ClosablePublisher implements Closeable {

  private final Publisher publisher;

  public ClosablePublisher(Publisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void close() throws IOException {

  }

  public Publisher getPublisher() {
    return publisher;
  }
}
