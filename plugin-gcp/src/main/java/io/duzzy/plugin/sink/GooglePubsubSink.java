package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import io.duzzy.core.DuzzyRowKey;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.EventSink;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Documentation(
    identifier = "io.duzzy.plugin.sink.GooglePubsubSink",
    description = "Sink data to Google Cloud Pub/Sub",
    module = "io.duzzy.plugin-gcp",
    duzzyType = DuzzyType.SINK,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "project_id",
            aliases = {"projectId", "project-id"},
            description = "The GCP project ID"
        ),
        @Parameter(
            name = "topic_name",
            aliases = {"topicName", "topic-name"},
            description = "The Pub/Sub topic name"
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.GooglePubsubSink"
          project_id: "my-project-id"
          topic_name: "my-topic"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class GooglePubsubSink extends EventSink {

  private final String topicName;
  private final String projectId;
  private Publisher publisher;

  @JsonCreator
  public GooglePubsubSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("project_id")
      @JsonAlias({"projectId", "project-id"})
      String projectId,
      @JsonProperty("topic_name")
      @JsonAlias({"topicName", "topic-name"})
      String topicName
  ) {
    super(serializer);
    this.projectId = projectId;
    this.topicName = topicName;
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    super.init(duzzySchema);
    this.publisher = buildPublisher();
  }

  @Override
  protected void sendEvent(DuzzyRowKey eventKey)
      throws IOException, ExecutionException, InterruptedException {
    final ByteString data = ByteString.copyFrom(getOutputStream().toByteArray());
    final PubsubMessage.Builder builder = PubsubMessage.newBuilder().setData(data);
    if (eventKey.isPresent()) {
      builder.setMessageId(eventKey.asString());
    }
    final PubsubMessage pubsubMessage = builder.build();

    final ApiFuture<String> future = publisher.publish(pubsubMessage);
    future.get();
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new GooglePubsubSink(getSerializer().fork(threadId), projectId, topicName);
  }

  Publisher buildPublisher() throws IOException {
    return Publisher.newBuilder(TopicName.of(projectId, topicName)).build();
  }
}
