package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.AzureCliCredentialBuilder;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
import com.azure.storage.file.datalake.DataLakeFileClient;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
import com.azure.storage.file.datalake.DataLakeServiceVersion;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.OutputStream;

@Documentation(
    identifier = "io.duzzy.plugin.sink.AzureAdlsSink",
    description = "Sink data to a Azure ADLS container",
    module = "io.duzzy.plugin-azure-adls",
    duzzyType = DuzzyType.SINK,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "service_version",
            aliases = {"serviceVersion", "service-version"},
            description = "The Azure Blob Storage service version to use, "
                + "leave null to use the latest version. Example: V2025_01_05"
        ),
        @Parameter(
            name = "endpoint",
            description = "The endpoint URL for the Azure Blob Storage service, "
                + "used if connectionString is not provided. "
                + "Example: https://<account_name>.blob.core.windows.net/"
        ),
        @Parameter(
            name = "connection_string",
            aliases = {"connectionString", "connection-string"},
            description = "The connection string for the Azure Blob Storage service, "
                + "if not set, endpoint is used. "
                + "Example: DefaultEndpointsProtocol=https;"
                + "AccountName=<account_name>;"
                + "AccountKey=<account_key>;"
                + "EndpointSuffix=core.windows.net"
        ),
        @Parameter(
            name = "tenant_id",
            aliases = {"tenantId", "tenant-id"},
            description = "The Azure AD tenant ID, "
                + "leave null if using DefaultAzureCredentialBuilder "
                + "(e.g., running in Azure environment or with Azure CLI logged in)"
        ),
        @Parameter(
            name = "client_id",
            aliases = {"clientId", "client-id"},
            description = "The Azure AD client ID, "
                + "leave null if using DefaultAzureCredentialBuilder "
                + "(e.g., running in Azure environment or with Azure CLI logged in)"
        ),
        @Parameter(
            name = "client_secret",
            aliases = {"clientSecret", "client-secret"},
            description = "The Azure AD client secret, "
                + "leave null if using DefaultAzureCredentialBuilder "
                + "(e.g., running in Azure environment or with Azure CLI logged in)"
        ),
        @Parameter(
            name = "create_container_if_not_exists",
            aliases = {"createContainerIfNotExists", "create-container-if-not-exists"},
            description = "Whether to create the container if it does not exist",
            defaultValue = "true"
        ),
        @Parameter(
            name = "container",
            description = "The name of the Azure Blob Storage container"
        ),
        @Parameter(
            name = "blob_name",
            aliases = {"blobName", "blob-name"},
            description = "The name of the blob to write data to"
        )
    }
)
public class AzureAdlsSink extends Sink {

  private final String tenantId;
  private final String clientId;
  private final String clientSecret;
  private final String connectionString;
  private final String serviceVersion;
  private final String endpoint;
  private final String container;
  private final String blobName;
  private final Boolean createContainerIfNotExists;

  @JsonCreator
  public AzureAdlsSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("service_version")
      @JsonAlias({"serviceVersion", "service-version"})
      String serviceVersion,
      @JsonProperty("endpoint")
      String endpoint,
      @JsonProperty("connection_string")
      @JsonAlias({"connectionString", "connection-string"})
      String connectionString,
      @JsonProperty("tenant_id")
      @JsonAlias({"tenantId", "tenant-id"})
      String tenantId,
      @JsonProperty("client_id")
      @JsonAlias({"clientId", "client-id"})
      String clientId,
      @JsonProperty("client_secret")
      @JsonAlias({"clientSecret", "client-secret"})
      String clientSecret,
      @JsonProperty("create_container_if_not_exists")
      @JsonAlias({"create-container-if-not-exists", "createContainerIfNotExists"})
      Boolean createContainerIfNotExists,
      @JsonProperty("container")
      String container,
      @JsonProperty("blob_name")
      @JsonAlias({"blobName", "blob-name"})
      String blobName
  ) {
    super(serializer);
    this.serviceVersion = serviceVersion;
    this.endpoint = endpoint;
    this.connectionString = connectionString;
    this.tenantId = tenantId;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.container = container;
    this.blobName = blobName;
    this.createContainerIfNotExists =
        createContainerIfNotExists == null || createContainerIfNotExists;
  }

  @Override
  public OutputStream outputStreamSupplier() {
    final DataLakeServiceClient dataLakeServiceClient = new DataLakeServiceClientBuilder()
        .serviceVersion(getDataLakeServiceVersion())
        .credential(getCredential())
        .endpoint(endpoint)
        .buildClient();

    final DataLakeFileSystemClient fileSystemClient =
        dataLakeServiceClient.getFileSystemClient(container);

    if (createContainerIfNotExists) {
      fileSystemClient.createIfNotExists();
    }

    final DataLakeFileClient fileClient = fileSystemClient.getFileClient(blobName);
//    fileClient.createIfNotExists();
    return fileClient.getOutputStream();
//
//    final BlobContainerClientBuilder blobContainerClientBuilder = new BlobContainerClientBuilder()
//        .serviceVersion(getBlobServiceVersion())
//        .credential(getCredential())
//        .containerName(container);
//    if (connectionString != null) {
//      blobContainerClientBuilder.connectionString(connectionString);
//    } else if (endpoint != null) {
//      blobContainerClientBuilder.endpoint(endpoint);
//    } else {
//      throw new IllegalArgumentException("Either connectionString or endpoint must be provided.");
//    }
//    final BlobContainerClient blobContainerClient = blobContainerClientBuilder.buildClient();
//
//    if (createContainerIfNotExists) {
//      blobContainerClient.createIfNotExists();
//    }
//
//    return blobContainerClient.getBlobClient(blobName).getBlockBlobClient().getBlobOutputStream();
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new AzureAdlsSink(
        getSerializer().fork(threadId),
        serviceVersion,
        endpoint,
        connectionString,
        tenantId,
        clientId,
        clientSecret,
        createContainerIfNotExists,
        container,
        addFilePart(blobName, threadId)
    );
  }

  private BlobServiceVersion getBlobServiceVersion() {
    return serviceVersion == null ? BlobServiceVersion.getLatest() :
        BlobServiceVersion.valueOf(serviceVersion);
  }

  private DataLakeServiceVersion getDataLakeServiceVersion() {
    return serviceVersion == null ? DataLakeServiceVersion.getLatest() :
        DataLakeServiceVersion.valueOf(serviceVersion);
  }

  private TokenCredential getCredential() {
    if (tenantId == null || clientId == null || clientSecret == null) {
      //      return new DefaultAzureCredentialBuilder().build();
      return new AzureCliCredentialBuilder().build();
    } else {
      return new ClientSecretCredentialBuilder()
          .tenantId(tenantId)
          .clientId(clientId)
          .clientSecret(clientSecret)
          .build();
    }
  }
}