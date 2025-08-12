package io.duzzy.plugin.sink;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
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
    identifier = "io.duzzy.plugin.sink.AzureBlobStorageSink",
    description = "Sink data to a Azure Blob Storage container",
    module = "io.duzzy.plugin-azure",
    duzzyType = DuzzyType.SINK,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "azure_auth_type",
            aliases = {"azureAuthType", "azure-auth-type"},
            description = "The type of authentication to use for Azure Blob Storage. "
                + "Options are 'DEFAULT_AZURE_CREDENTIALS' (default) or 'CONNECTION_STRING'. "
                + "If 'CONNECTION_STRING' is used, "
                + "the connection string must be set "
                + "in the environment variable AZURE_STORAGE_CONNECTION_STRING.",
            defaultValue = "DEFAULT_AZURE_CREDENTIALS"
        ),
        @Parameter(
            name = "account_name",
            aliases = {"accountName", "account-name"},
            description = "The Azure Storage account name"
        ),
        @Parameter(
            name = "service_version",
            aliases = {"serviceVersion", "service-version"},
            description = "The Azure Blob Storage service version to use. "
                + "If not specified, the latest version will be used.",
            defaultValue = "LATEST"
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
        ),
        @Parameter(
            name = "compression_algorithm",
            aliases = {"compressionAlgorithm", "compression-algorithm"},
            description = "The compression algorithm to use for the file, "
                + "available options are: NONE, BZIP2, GZIP, ZSTD. "
                + "If not specified, no compression will be applied.",
            defaultValue = "NONE"
        ),
        @Parameter(
            name = "size_of_file",
            aliases = {"sizeOfFile", "size-of-file"},
            description = "The size of the file in bytes. "
                + "If not specified, the file will not be limited by size."
        ),
        @Parameter(
            name = "rows_per_file",
            aliases = {"rowsPerFile", "rows-per-file"},
            description = "The number of rows per file. "
                + "If not specified, the file will not be limited by number of rows."
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.AzureBlobStorageSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
          azureAuthType: "DEFAULT_AZURE_CREDENTIALS"
          accountName: "myaccount"
          createContainerIfNotExists: true
          container: "mycontainer"
          blobName: "myblob.txt"
          compressionAlgorithm: "NONE"
        """
)
public class AzureBlobStorageSink extends AzureStorageSink {

  @JsonCreator
  public AzureBlobStorageSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("azure_auth_type")
      @JsonAlias({"azureAuthType", "azure-auth-type"})
      AzureAuthType azureAuthType,
      @JsonProperty("account_name")
      @JsonAlias({"account-name", "accountName"})
      String accountName,
      @JsonProperty("service_version")
      @JsonAlias({"serviceVersion", "service-version"})
      String serviceVersion,
      @JsonProperty("create_container_if_not_exists")
      @JsonAlias({"create-container-if-not-exists", "createContainerIfNotExists"})
      Boolean createContainerIfNotExists,
      @JsonProperty("container")
      String container,
      @JsonProperty("blob_name")
      @JsonAlias({"blobName", "blob-name"})
      String blobName,
      @JsonProperty("compression_algorithm")
      @JsonAlias({"compressionAlgorithm", "compression-algorithm"})
      CompressionAlgorithm compressionAlgorithm,
      @JsonProperty("size_of_file")
      @JsonAlias({"sizeOfFile", "size-of-file"})
      Long sizeOfFile,
      @JsonProperty("rows_per_file")
      @JsonAlias({"rowsPerFile", "rows-per-file"})
      Long rowsPerFile
  ) {
    super(
        serializer,
        azureAuthType,
        accountName,
        "https://" + accountName + ".blob.core.windows.net",
        serviceVersion,
        createContainerIfNotExists,
        container,
        blobName,
        compressionAlgorithm,
        sizeOfFile,
        rowsPerFile
    );
  }

  @Override
  protected OutputStream createOutputStream() {
    final BlobServiceClientBuilder serviceClientBuilder = new BlobServiceClientBuilder();
    if (getAzureAuthType() == AzureAuthType.DEFAULT_AZURE_CREDENTIALS) {
      serviceClientBuilder
          .credential(new DefaultAzureCredentialBuilder().build())
          .endpoint(getEndpoint());
    } else if (getAzureAuthType() == AzureAuthType.CONNECTION_STRING) {
      serviceClientBuilder.connectionString(getConnectionString());
    }
    if (getServiceVersion() != null && !getServiceVersion().isEmpty()) {
      serviceClientBuilder.serviceVersion(BlobServiceVersion.valueOf(getServiceVersion()));
    }
    final BlobServiceClient blobServiceClient = serviceClientBuilder.buildClient();

    if (getCreateContainerIfNotExists()) {
      blobServiceClient.createBlobContainerIfNotExists(getContainer());
    }

    return blobServiceClient
        .getBlobContainerClient(getContainer())
        .getBlobClient(incrementedName())
        .getBlockBlobClient()
        .getBlobOutputStream();
  }

  @Override
  public Sink fork(long id) throws Exception {
    return new AzureBlobStorageSink(
        getSerializer().fork(id),
        getAzureAuthType(),
        getAccountName(),
        getServiceVersion(),
        getCreateContainerIfNotExists(),
        getContainer(),
        forkedName(id),
        getCompressionAlgorithm(),
        getSizeOfFile(),
        getRowsPerFile()
    );
  }
}