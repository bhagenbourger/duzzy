package io.duzzy.plugin.sink;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.tests.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.azure.AzuriteContainer;
import org.testcontainers.utility.DockerImageName;

public class AzureBlobStorageSinkTest {

  private static final AzuriteContainer azurite =
      new AzuriteContainer(DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:3.33.0"));

  @BeforeAll
  static void beforeAll() {
    azurite.start();
  }

  @AfterAll
  static void afterAll() {
    azurite.stop();
  }

  @Test
  public void testSink() throws Exception {
    final String containerName = "blob-container";
    final String path = "blob.json";
    final String path1 = "blob_1.json";
    final AzureBlobStorageSink sink = spy(new AzureBlobStorageSink(
        new JsonSerializer(),
        "devstoreaccount1",
        true,
        containerName,
        path
    ));

    doReturn(client()).when(sink).buildBlobContainerClient();
    doReturn(azurite.getConnectionString()).when(sink).getEndpoint();

    sink.init(null);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    final AzureBlobStorageSink fork = (AzureBlobStorageSink) spy(sink.fork(1L));

    doReturn(client()).when(fork).buildBlobContainerClient();
    doReturn(azurite.getConnectionString()).when(fork).getEndpoint();

    fork.init(null);
    fork.write(Data.getDataOne());
    fork.write(Data.getDataTwo());
    fork.close();

    final BlobContainerClient blobContainerClient = client();

    final String result = blobContainerClient.getBlobClient(path).downloadContent().toString();
    final String result1 = blobContainerClient.getBlobClient(path1).downloadContent().toString();


    assertEquals("{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}", result);
    assertEquals("{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}", result1);
  }

  private static BlobContainerClient client() {
    return new BlobContainerClientBuilder()
        .connectionString(azurite.getConnectionString())
        .serviceVersion(BlobServiceVersion.V2025_01_05)
        .containerName("blob-container")
        .buildClient();
  }
}
