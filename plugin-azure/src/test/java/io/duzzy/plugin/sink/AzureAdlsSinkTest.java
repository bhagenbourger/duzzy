package io.duzzy.plugin.sink;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.tests.Data;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.azure.AzuriteContainer;
import org.testcontainers.utility.DockerImageName;

public class AzureAdlsSinkTest {

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
    final String containerName = "test-container";
    final String path = "test.json";
    final String path1 = "test_1.json";
    final AzureAdlsSink sink = new AzureAdlsSink(
        new JsonSerializer(),
        BlobServiceVersion.V2025_01_05.toString(),
        null,
        azurite.getConnectionString(),
        null,
        null,
        null,
        true,
        containerName,
        path
    );

    sink.init(null);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    final Sink fork = sink.fork(1L);
    fork.init(null);
    fork.write(Data.getDataOne());
    fork.write(Data.getDataTwo());
    fork.close();

    final BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
        .connectionString(azurite.getConnectionString())
        .serviceVersion(BlobServiceVersion.V2025_01_05)
        .containerName(containerName)
        .buildClient();

    final String result = blobContainerClient.getBlobClient(path).downloadContent().toString();
    final String result1 = blobContainerClient.getBlobClient(path1).downloadContent().toString();


    assertEquals("{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}", result);
    assertEquals("{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}", result1);
  }

  @Test
  void shouldThrowAnErrorWhenNeitherConnectionStringNorEndpointIsProvided() {
    final AzureAdlsSink sink = new AzureAdlsSink(
        new JsonSerializer(),
        null,
        null,
        null,
        null,
        null,
        null,
        true,
        "test-container",
        "test.json"
    );

    assertThatThrownBy(sink::outputStreamSupplier)
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Either connectionString or endpoint must be provided.");
  }
}