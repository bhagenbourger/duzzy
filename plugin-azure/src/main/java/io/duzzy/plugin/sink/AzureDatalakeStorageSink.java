package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.file.datalake.DataLakeFileSystemClient;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
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
            name = "account_name",
            aliases = {"accountName", "account-name"},
            description = "The Azure Storage account name"
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
public class AzureDatalakeStorageSink extends AzureStorageSink {

  @JsonCreator
  public AzureDatalakeStorageSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("account_name")
      @JsonAlias({"account-name", "accountName"})
      String accountName,
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
        accountName,
        "https://" + accountName + ".dfs.core.windows.net",
        createContainerIfNotExists,
        container,
        blobName
    );
  }

  @Override
  public OutputStream outputStreamSupplier() {
    final DataLakeServiceClient dataLakeServiceClient = buildDataLakeServiceClient();

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
        getAccountName(),
        getCreateContainerIfNotExists(),
        getContainer(),
        addFilePart(getBlobName(), threadId)
    );
  }

  DataLakeServiceClient buildDataLakeServiceClient() {
    return new DataLakeServiceClientBuilder()
        .credential(new DefaultAzureCredentialBuilder().build())
        .endpoint(getEndpoint())
        .buildClient();
  }
}