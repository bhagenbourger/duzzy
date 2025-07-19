package io.duzzy.plugin.sink;

import static io.duzzy.plugin.sink.AzureAuthType.DEFAULT_AZURE_CREDENTIALS;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.EventSink;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Documentation(
    identifier = "io.duzzy.plugin.sink.AzureEventHubSink",
    description = "Sink data to an Azure Event Hub",
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
            description = "The type of authentication to use for Azure Event Hub. "
                + "Options are 'DEFAULT_AZURE_CREDENTIALS' (default) or 'CONNECTION_STRING'. "
                + "If 'CONNECTION_STRING' is used, "
                + "the connection string must be set "
                + "in the environment variable AZURE_EVENT_HUB_CONNECTION_STRING.",
            defaultValue = "DEFAULT_AZURE_CREDENTIALS"
        ),
        @Parameter(
            name = "event_hub_name",
            aliases = {"eventHubName", "event-hub-name"},
            description = "The name of the Azure Event Hub, "
                + "required only if azure_auth_type is DEFAULT_AZURE_CREDENTIALS."
        ),
        @Parameter(
            name = "fully_qualified_namespace",
            aliases = {"fullyQualifiedNamespace", "fully-qualified-namespace"},
            description = "The fully qualified namespace of the Azure Event Hub, "
                + "required only if azure_auth_type is DEFAULT_AZURE_CREDENTIALS."
        ),
        @Parameter(
            name = "fail_on_error",
            aliases = {"failOnError", "fail-on-error"},
            description = "Whether to fail on error when sending events. "
                + "If true, an IOException will be thrown if an event cannot be sent.",
            defaultValue = "true"
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.AzureEventHubSink"
          azure_auth_type: "DEFAULT_AZURE_CREDENTIALS"
          event_hub_name: "my-event-hub"
          fully_qualified_namespace: "my-namespace.servicebus.windows.net"
          fail_on_error: true
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class AzureEventHubSink extends EventSink<EventHubProducerClient> {

  private static final Logger logger = LoggerFactory.getLogger(AzureEventHubSink.class);

  private final String eventHubName;
  private final String fullyQualifiedNamespace;
  private final AzureAuthType azureAuthType;
  private final Boolean failOnError;

  @JsonCreator
  public AzureEventHubSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("azure_auth_type")
      @JsonAlias({"azureAuthType", "azure-auth-type"})
      AzureAuthType azureAuthType,
      @JsonProperty("event_hub_name")
      @JsonAlias({"eventHubName", "event-hub-name"})
      String eventHubName,
      @JsonProperty("fully_qualified_namespace")
      @JsonAlias({"fullyQualifiedNamespace", "fully-qualified-namespace"})
      String fullyQualifiedNamespace,
      @JsonProperty("fail_on_error")
      @JsonAlias({"failOnError", "fail-on-error"})
      Boolean failOnError
  ) {
    super(serializer);
    this.eventHubName = eventHubName;
    this.fullyQualifiedNamespace = fullyQualifiedNamespace;
    this.azureAuthType = azureAuthType == null ? DEFAULT_AZURE_CREDENTIALS : azureAuthType;
    this.failOnError = failOnError == null || failOnError;
  }

  @Override
  protected EventHubProducerClient buildProducer() {
    final EventHubClientBuilder eventHubClientBuilder = new EventHubClientBuilder();
    if (azureAuthType == DEFAULT_AZURE_CREDENTIALS) {
      eventHubClientBuilder.credential(new DefaultAzureCredentialBuilder().build());
    }
    if (eventHubName != null && !eventHubName.isEmpty()) {
      eventHubClientBuilder.eventHubName(eventHubName);
    }
    if (fullyQualifiedNamespace != null && !fullyQualifiedNamespace.isEmpty()) {
      eventHubClientBuilder.fullyQualifiedNamespace(fullyQualifiedNamespace);
    }
    return eventHubClientBuilder.buildProducerClient();
  }

  @Override
  protected void sendEvent() throws IOException {
    final EventDataBatch batch = getProducer().createBatch();
    final EventData eventData = new EventData(getOutputStream().toString(StandardCharsets.UTF_8));
    if (batch.tryAdd(eventData)) {
      getProducer().send(batch);
    } else {
      final String message = "Failed to add event to batch: " + eventData.getBodyAsString();
      logger.warn(message);
      if (failOnError) {
        throw new IOException(message);
      }
    }
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new AzureEventHubSink(
        getSerializer().fork(threadId),
        azureAuthType,
        eventHubName,
        fullyQualifiedNamespace,
        failOnError
    );
  }
}
