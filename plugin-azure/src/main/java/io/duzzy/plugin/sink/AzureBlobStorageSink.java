package io.duzzy.plugin.sink;

import static io.duzzy.core.sink.FileSink.addFilePart;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
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
public class AzureBlobStorageSink extends AzureStorageSink {

  @JsonCreator
  public AzureBlobStorageSink(
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
    super(serializer,
        accountName,
        "https://" + accountName + ".blob.core.windows.net",
        createContainerIfNotExists,
        container,
        blobName);
  }

  @Override
  public OutputStream outputStreamSupplier() {
    final BlobContainerClient blobContainerClient = buildBlobContainerClient();

    if (getCreateContainerIfNotExists()) {
      blobContainerClient.createIfNotExists();
    }

    return blobContainerClient
        .getBlobClient(getBlobName())
        .getBlockBlobClient()
        .getBlobOutputStream();
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new AzureBlobStorageSink(
        getSerializer().fork(threadId),
        getAccountName(),
        getCreateContainerIfNotExists(),
        getContainer(),
        addFilePart(getBlobName(), threadId)
    );
  }

  BlobContainerClient buildBlobContainerClient() {
    return new BlobContainerClientBuilder()
        .credential(new DefaultAzureCredentialBuilder().build())
        .endpoint(getEndpoint())
        .containerName(getContainer())
        .buildClient();
  }
}