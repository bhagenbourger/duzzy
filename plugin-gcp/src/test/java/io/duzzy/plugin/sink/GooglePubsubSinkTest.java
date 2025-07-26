package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PullRequest;
import com.google.pubsub.v1.PullResponse;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.SubscriptionName;
import com.google.pubsub.v1.TopicName;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.CsvSerializer;
import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.tests.Data;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PubSubEmulatorContainer;
import org.testcontainers.utility.DockerImageName;

public class GooglePubsubSinkTest {
  private static final PubSubEmulatorContainer pubsub = new PubSubEmulatorContainer(
      DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:441.0.0-emulators")
  );

  @BeforeAll
  static void beforeAll() {
    pubsub.start();
  }

  @AfterAll
  static void afterAll() {
    pubsub.stop();
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/pubsub-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(GooglePubsubSink.class);
    assertThat(sink.getSerializer()).isInstanceOf(CsvSerializer.class);
  }

  @Test
  void testGooglePubsubSink() throws Exception {
    final String projectId = "test-project";
    final String topicName = "test-topic";
    final String subscriptionId = "test-subscription";
    final TransportChannelProvider channelProvider = getTransportChannelProvider();
    createTopic(projectId, topicName, channelProvider);
    createSubscription(subscriptionId, projectId, topicName, channelProvider);

    final GooglePubsubSink sink = spy(new GooglePubsubSink(
        new JsonSerializer(),
        projectId,
        topicName
    ));
    doReturn(
        Publisher
            .newBuilder(TopicName.of(projectId, topicName))
            .setChannelProvider(channelProvider)
            .setCredentialsProvider(NoCredentialsProvider.create())
            .build()
    ).when(sink).buildPublisher();
    sink.init(null);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    final SubscriberStubSettings subscriberStubSettings = SubscriberStubSettings
        .newBuilder()
        .setTransportChannelProvider(channelProvider)
        .setCredentialsProvider(NoCredentialsProvider.create())
        .build();
    try (final SubscriberStub subscriber = GrpcSubscriberStub.create(subscriberStubSettings)) {
      final PullRequest pullRequest = PullRequest
          .newBuilder()
          .setMaxMessages(2)
          .setSubscription(ProjectSubscriptionName.format(projectId, subscriptionId))
          .build();
      final PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);

      assertThat(pullResponse.getReceivedMessagesList()).hasSize(2);
      assertThat(pullResponse.getReceivedMessages(0).getMessage().getData().toStringUtf8())
          .isEqualTo(Data.getDataOneAsJsonString());
      assertThat(pullResponse.getReceivedMessages(1).getMessage().getData().toStringUtf8())
          .isEqualTo(Data.getDataTwoAsJsonString());
    }
  }

  private static TransportChannelProvider getTransportChannelProvider() {
    final ManagedChannel channel = ManagedChannelBuilder
        .forTarget(pubsub.getEmulatorEndpoint())
        .usePlaintext()
        .build();
    return FixedTransportChannelProvider.create(
        GrpcTransportChannel.create(channel)
    );
  }

  private static void createTopic(
      String projectId,
      String topicId,
      TransportChannelProvider channelProvider
  ) throws IOException {
    final TopicAdminSettings topicAdminSettings = TopicAdminSettings
        .newBuilder()
        .setTransportChannelProvider(channelProvider)
        .setCredentialsProvider(NoCredentialsProvider.create())
        .build();
    try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
      topicAdminClient.createTopic(TopicName.of(projectId, topicId));
    }
  }

  private static void createSubscription(
      String subscriptionId,
      String projectId,
      String topicId,
      TransportChannelProvider channelProvider
  ) throws IOException {
    final SubscriptionAdminSettings subscriptionAdminSettings = SubscriptionAdminSettings
        .newBuilder()
        .setTransportChannelProvider(channelProvider)
        .setCredentialsProvider(NoCredentialsProvider.create())
        .build();
    try (final SubscriptionAdminClient client =
             SubscriptionAdminClient.create(subscriptionAdminSettings)) {
      client.createSubscription(
          SubscriptionName.of(projectId, subscriptionId),
          TopicName.of(projectId, topicId),
          PushConfig.getDefaultInstance(),
          10
      );
    }

  }


}
