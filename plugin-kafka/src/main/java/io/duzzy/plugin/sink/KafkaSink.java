package io.duzzy.plugin.sink;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRowKey;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.EventSink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

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
        ),
        @Parameter(
            name = "key_serializer",
            aliases = {"keySerializer", "key-serializer"},
            description = "The serializer for the key "
                + "(default: org.apache.kafka.common.serialization.StringSerializer)"
        ),
        @Parameter(
            name = "value_serializer",
            aliases = {"valueSerializer", "value-serializer"},
            description = "The serializer for the value "
                + "(default: org.apache.kafka.common.serialization.ByteArraySerializer)"
        ),
        @Parameter(
            name = "custom_properties",
            aliases = {"customProperties", "custom-properties"},
            description = "Custom properties for the Kafka producer"
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
          keySerializer: "org.apache.kafka.common.serialization.StringSerializer"
          valueSerializer: "org.apache.kafka.common.serialization.ByteArraySerializer"
        """
)
public class KafkaSink extends EventSink {

  private final String topic;
  private final String bootstrapServers;
  private final String keySerializer;
  private final String valueSerializer;
  private final Map<String, String> customProperties;
  private final KafkaProducer<Object, Object> producer;

  @JsonCreator
  public KafkaSink(
      @JsonProperty("serializer")
      Serializer<?> serializer,
      @JsonProperty("topic")
      String topic,
      @JsonProperty("bootstrap_servers")
      @JsonAlias({"bootstrapServers", "bootstrap-servers"})
      String bootstrapServers,
      @JsonProperty("key_serializer")
      @JsonAlias({"keySerializer", "key-serializer"})
      String keySerializer,
      @JsonProperty("value_serializer")
      @JsonAlias({"valueSerializer", "value-serializer"})
      String valueSerializer,
      @JsonProperty("custom_properties")
      @JsonAlias({"customProperties", "custom-properties"})
      Map<String, String> customProperties
  ) {
    super(serializer);
    this.topic = topic;
    this.bootstrapServers = bootstrapServers;
    this.keySerializer = keySerializer != null ? keySerializer : StringSerializer.class.getName();
    this.valueSerializer =
        valueSerializer != null ? valueSerializer : ByteArraySerializer.class.getName();
    this.customProperties = customProperties != null ? customProperties : Map.of();
    this.producer = new KafkaProducer<>(buildProperties());
  }

  @Override
  protected void sendEvent(
      DuzzyRowKey eventKey,
      ByteArrayOutputStream outputStream
  ) {
    final ProducerRecord<Object, Object> record = new ProducerRecord<>(
        topic,
        writeEventKey(eventKey),
        writeEventValue(outputStream)
    );
    this.producer.send(record);
  }

  private Object writeEventValue(ByteArrayOutputStream outputStream) {
    if (Objects.equals(valueSerializer, StringSerializer.class.getName())) {
      return outputStream.toString(UTF_8);
    } else if (Objects.equals(valueSerializer, ByteArraySerializer.class.getName())) {
      return outputStream.toByteArray();
    } else {
      throw new IllegalArgumentException("Unsupported value serializer: " + valueSerializer);
    }
  }

  private Object writeEventKey(DuzzyRowKey eventKey) {
    if (Objects.equals(keySerializer, StringSerializer.class.getName())) {
      return eventKey.asString();
    } else if (Objects.equals(keySerializer, ByteArraySerializer.class.getName())) {
      return eventKey.asBytes();
    } else {
      throw new IllegalArgumentException("Unsupported value serializer: " + valueSerializer);
    }
  }

  @Override
  public KafkaSink fork(long id) throws Exception {
    return new KafkaSink(
        getSerializer().fork(id),
        topic,
        bootstrapServers,
        keySerializer,
        valueSerializer,
        customProperties
    );
  }

  private Properties buildProperties() {
    final Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
    props.putAll(customProperties);
    return props;
  }
}
