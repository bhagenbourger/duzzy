package io.duzzy.plugin.sink;

import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;

public abstract class AzureStorageSink extends Sink {

  private final String accountName;
  private final String endpoint;
  private final String container;
  private final String blobName;
  private final Boolean createContainerIfNotExists;

  public AzureStorageSink(
      Serializer<?> serializer,
      String accountName,
      String endpoint,
      Boolean createContainerIfNotExists,
      String container,
      String blobName
  ) {
    super(serializer);
    this.accountName = accountName;
    this.endpoint = endpoint;
    this.container = container;
    this.blobName = blobName;
    this.createContainerIfNotExists =
        createContainerIfNotExists == null || createContainerIfNotExists;
  }

  protected String getAccountName() {
    return accountName;
  }

  protected String getContainer() {
    return container;
  }

  protected String getBlobName() {
    return blobName;
  }

  protected Boolean getCreateContainerIfNotExists() {
    return createContainerIfNotExists;
  }

  protected String getEndpoint() {
    return endpoint;
  }
}
