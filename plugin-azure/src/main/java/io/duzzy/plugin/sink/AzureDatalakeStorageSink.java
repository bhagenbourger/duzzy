package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.azure.identity.DefaultAzureCredentialBuilder;
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
    identifier = "io.duzzy.plugin.sink.AzureDatalakeStorageSink",
    description = "Sink data to a Azure Datalake Storage (ADLS) container",
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
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.AzureDatalakeStorageSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
          azureAuthType: "DEFAULT_AZURE_CREDENTIALS"
          accountName: "myaccount"
          createContainerIfNotExists: true
          container: "mycontainer"
          blobName: "myblob.txt"
        """
)
public class AzureDatalakeStorageSink extends AzureStorageSink {

  @JsonCreator
  public AzureDatalakeStorageSink(
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
      String blobName
  ) {
    super(
        serializer,
        azureAuthType,
        accountName,
        "https://" + accountName + ".dfs.core.windows.net",
        serviceVersion,
        createContainerIfNotExists,
        container,
        blobName
    );
  }

  @Override
  public OutputStream outputStreamSupplier() {
    final DataLakeServiceClientBuilder serviceClientBuilder = new DataLakeServiceClientBuilder();
    if (getAzureAuthType() == AzureAuthType.DEFAULT_AZURE_CREDENTIALS) {
      serviceClientBuilder
          .credential(new DefaultAzureCredentialBuilder().build())
          .endpoint(getEndpoint());
    } else if (getAzureAuthType() == AzureAuthType.CONNECTION_STRING) {
      serviceClientBuilder.connectionString(getConnectionString());
    }
    if (getServiceVersion() != null && !getServiceVersion().isEmpty()) {
      serviceClientBuilder.serviceVersion(DataLakeServiceVersion.valueOf(getServiceVersion()));
    }
    final DataLakeServiceClient dataLakeServiceClient = serviceClientBuilder.buildClient();
    final DataLakeFileSystemClient fileSystemClient =
        dataLakeServiceClient.getFileSystemClient(getContainer());

    if (getCreateContainerIfNotExists()) {
      fileSystemClient.createIfNotExists();
    }

    return fileSystemClient.getFileClient(getBlobName()).getOutputStream();
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new AzureDatalakeStorageSink(
        getSerializer().fork(threadId),
        getAzureAuthType(),
        getAccountName(),
        getServiceVersion(),
        getCreateContainerIfNotExists(),
        getContainer(),
        addFilePart(getBlobName(), threadId)
    );
  }
}