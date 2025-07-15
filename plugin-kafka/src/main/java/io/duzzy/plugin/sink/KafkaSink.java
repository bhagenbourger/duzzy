package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.EventSink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

@Documentation(
    identifier = "io.duzzy.plugin.sink.KafkaSink",
    description = "Sink data to a Kafka topic",
    module = "io.duzzy.plugin-kafka",
    duzzyType = DuzzyType.SINK,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "topic",
            description = "The Kafka topic"
        ),
        @Parameter(
            name = "bootstrap_servers",
            aliases = {"bootstrapServers", "bootstrap-servers"},
            description = "The Kafka bootstrap servers"
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.KafkaSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
          topic: "my-topic"
          bootstrapServers: "localhost:9092"
        """
)
public class KafkaSink extends EventSink<KafkaProducer<String, byte[]>> {

  private final String topic;
  private final String bootstrapServers;

  @JsonCreator
  public KafkaSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("topic")
      String topic,
      @JsonProperty("bootstrap_servers")
      @JsonAlias({"bootstrapServers", "bootstrap-servers"})
      String bootstrapServers
  ) {
    super(serializer);
    this.topic = topic;
    this.bootstrapServers = bootstrapServers;
  }

  @Override
  protected KafkaProducer<String, byte[]> buildProducer() {
    return new KafkaProducer<>(buildProperties(bootstrapServers));
  }

  @Override
  protected void sendEvent() throws IOException {
    final ProducerRecord<String, byte[]> record = new ProducerRecord<>(
        topic,
        null,
        getOutputStream().toByteArray()
    );
    getProducer().send(record);
  }

  @Override
  public KafkaSink fork(Long threadId) throws Exception {
    return new KafkaSink(getSerializer().fork(threadId), topic, bootstrapServers);
  }

  private static Properties buildProperties(String bootstrapServers) {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer"
    );
    props.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.ByteArraySerializer"
    );
    return props;
  }
}
