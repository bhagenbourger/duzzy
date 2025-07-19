package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceVersion;
import com.azure.storage.file.datalake.DataLakeServiceClient;
import com.azure.storage.file.datalake.DataLakeServiceClientBuilder;
import com.azure.storage.file.datalake.DataLakeServiceVersion;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.tests.Data;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.azure.AzuriteContainer;
import org.testcontainers.utility.DockerImageName;

public class AzureDatalakeStorageSinkTest {

  private static final AzuriteContainer azurite =
      new AzuriteContainer(DockerImageName.parse("mcr.microsoft.com/azure-storage/azurite:3.33.0"));
  private static final String CONTAINER_NAME = "staging";

  @BeforeAll
  static void beforeAll() {
    azurite.start();
  }

  @AfterAll
  static void afterAll() {
    azurite.stop();
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/azure-datalake-storage.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(AzureDatalakeStorageSink.class);
    assertThat(sink.getSerializer()).isInstanceOf(JsonSerializer.class);
  }

  @Test
  public void testSink() throws Exception {
    System.setProperty("AZURE_STORAGE_CONNECTION_STRING", azurite.getConnectionString());
    final String path = "blob.json";
    final String path1 = "blob_1.json";
    final AzureDatalakeStorageSink sink = spy(new AzureDatalakeStorageSink(
        new JsonSerializer(),
        AzureAuthType.CONNECTION_STRING,
        "devstoreaccount1",
        DataLakeServiceVersion.V2025_01_05.name(),
        true,
        CONTAINER_NAME,
        path
    ));

    doReturn(azurite.getConnectionString()).when(sink).getEndpoint();

    sink.init(null);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    final AzureDatalakeStorageSink fork = (AzureDatalakeStorageSink) spy(sink.fork(1L));

    doReturn(azurite.getConnectionString()).when(fork).getEndpoint();

    fork.init(null);
    fork.write(Data.getDataOne());
    fork.write(Data.getDataTwo());
    fork.close();

    final BlobContainerClient blobContainerClient = new BlobContainerClientBuilder()
        .connectionString(azurite.getConnectionString())
        .serviceVersion(BlobServiceVersion.V2025_01_05)
        .containerName(CONTAINER_NAME)
        .buildClient();

    final String result = blobContainerClient.getBlobClient(path).downloadContent().toString();
    final String result1 = blobContainerClient.getBlobClient(path1).downloadContent().toString();


    assertEquals("{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}", result);
    assertEquals("{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}", result1);
  }

  private static DataLakeServiceClient client() {
    return new DataLakeServiceClientBuilder()
        .connectionString(azurite.getConnectionString())
        .serviceVersion(DataLakeServiceVersion.V2025_01_05)
        .buildClient();
  }
}
