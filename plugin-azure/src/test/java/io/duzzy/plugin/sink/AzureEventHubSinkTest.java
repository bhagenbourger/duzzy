package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionEvent;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.tests.Data;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.azure.AzuriteContainer;
import org.testcontainers.azure.EventHubsEmulatorContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.builder.Transferable;

public class AzureEventHubSinkTest {

  private static final String EVENT_HUB_NAME = "eh1";
  private static final String FULLY_QUALIFIED_NAMESPACE = "cg1";
  private static final String CONFIG = """
      {
        "UserConfig": {
          "NamespaceConfig": [
            {
              "Type": "EventHub",
              "Name": "emulatorNs1",
              "Entities": [
                {
                  "Name": "eh1",
                  "PartitionCount": "1",
                  "ConsumerGroups": [
                    {
                      "Name": "cg1"
                    }
                  ]
                }
              ]
            }
          ],\s
          "LoggingConfig": {
            "Type": "File"
          }
        }
      }
      """;

  private static final Network network = Network.newNetwork();
  private static final AzuriteContainer azurite =
      new AzuriteContainer("mcr.microsoft.com/azure-storage/azurite:3.33.0")
          .withNetwork(network);
  private static final EventHubsEmulatorContainer eventHubs =
      new EventHubsEmulatorContainer("mcr.microsoft.com/azure-messaging/eventhubs-emulator:2.0.1")
          .acceptLicense()
          .withConfig(Transferable.of(CONFIG))
          .withNetwork(network)
          .withAzuriteContainer(azurite);

  @BeforeAll
  static void beforeAll() {
    azurite.start();
    eventHubs.start();
  }

  @AfterAll
  static void afterAll() {
    eventHubs.stop();
    azurite.stop();
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/azure-event-hub.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(AzureEventHubSink.class);
  }

  @Test
  void test() throws Exception {
    System.setProperty("AZURE_EVENT_HUBS_CONNECTION_STRING", eventHubs.getConnectionString());
    final AzureEventHubSink sink = spy(new AzureEventHubSink(
        new JsonSerializer(),
        AzureAuthType.CONNECTION_STRING,
        EVENT_HUB_NAME,
        FULLY_QUALIFIED_NAMESPACE,
        true
    ));
    sink.init(null);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    final AzureEventHubSink fork = (AzureEventHubSink) spy(sink.fork(1L));
    fork.init(null);
    fork.write(Data.getDataOne());
    fork.write(Data.getDataTwo());
    fork.close();

    try (final EventHubConsumerClient client = new EventHubClientBuilder()
        .connectionString(eventHubs.getConnectionString())
        .eventHubName(EVENT_HUB_NAME)
        .consumerGroup(FULLY_QUALIFIED_NAMESPACE)
        .buildConsumerClient()) {

      final List<PartitionEvent> events = client
          .receiveFromPartition(
              "0",
              5,
              EventPosition.earliest(),
              Duration.of(1, ChronoUnit.SECONDS)
          )
          .stream()
          .toList();
      assertThat(events).hasSize(4);
      assertThat(events.getFirst().getData().getBodyAsString())
          .isEqualTo(Data.getDataOneAsJsonString());
      assertThat(events.get(1).getData().getBodyAsString())
          .isEqualTo(Data.getDataTwoAsJsonString());
      assertThat(events.get(2).getData().getBodyAsString())
          .isEqualTo(Data.getDataOneAsJsonString());
      assertThat(events.getLast().getData().getBodyAsString())
          .isEqualTo(Data.getDataTwoAsJsonString());
    }
  }
}
