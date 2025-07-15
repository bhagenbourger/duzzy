package io.duzzy.plugin.sink;

import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;

public abstract class AzureStorageSink extends Sink {

  private static final String AZURE_STORAGE_CONNECTION_STRING = "AZURE_STORAGE_CONNECTION_STRING";

  private final AzureAuthType azureAuthType;
  private final String accountName;
  private final String endpoint;
  private final String serviceVersion;
  private final String container;
  private final String blobName;
  private final Boolean createContainerIfNotExists;

  public AzureStorageSink(
      Serializer<?> serializer,
      AzureAuthType azureAuthType,
      String accountName,
      String endpoint,
      String serviceVersion,
      Boolean createContainerIfNotExists,
      String container,
      String blobName
  ) {
    super(serializer);
    this.azureAuthType = azureAuthType;
    this.accountName = accountName;
    this.endpoint = endpoint;
    this.serviceVersion = serviceVersion;
    this.container = container;
    this.blobName = blobName;
    this.createContainerIfNotExists =
        createContainerIfNotExists == null || createContainerIfNotExists;
  }

  protected String getConnectionString() {
    final String connectionStringFromEnv = System.getenv(AZURE_STORAGE_CONNECTION_STRING);
    final String connectionString =
        connectionStringFromEnv == null ? System.getProperty(AZURE_STORAGE_CONNECTION_STRING) :
            connectionStringFromEnv;
    if (connectionString == null || connectionString.isEmpty()) {
      throw new IllegalArgumentException(
          "Azure Storage connection string is not set. Please set the environment variable "
              + AZURE_STORAGE_CONNECTION_STRING + " or the system property "
              + AZURE_STORAGE_CONNECTION_STRING);
    }

    return connectionString;
  }

  protected AzureAuthType getAzureAuthType() {
    return azureAuthType;
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

  protected String getServiceVersion() {
    return serviceVersion;
  }
}
