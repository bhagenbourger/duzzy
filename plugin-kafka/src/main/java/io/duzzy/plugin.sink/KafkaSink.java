package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaSink extends Sink {

  private static final String DEFAULT_TOPIC = "duzzy_topic";
  private static final String DEFAULT_BOOTSTRAP_SERVERS = "127.0.0.1:9093";

  private final String topic;
  private final KafkaProducer<String, byte[]> producer;

  @JsonCreator
  public KafkaSink(
      @JsonProperty("serializer") Serializer<?> serializer,
      @JsonProperty("topic") String topic,
      @JsonProperty("bootstrap_servers")
      @JsonAlias({"bootstrapServers", "bootstrap-servers"})
      String bootstrapServers
  ) {
    super(serializer, new ByteArrayOutputStream());
    this.topic = topic == null ? DEFAULT_TOPIC : topic;
    this.producer = new KafkaProducer<>(
        buildProperties(bootstrapServers == null ? DEFAULT_BOOTSTRAP_SERVERS : bootstrapServers)
    );
  }

  @Override
  public void init(SchemaContext schemaContext) throws IOException {
    serializer.init(outputStream, schemaContext);
  }

  @Override
  public void write(DataItems data) throws Exception {
    super.write(data);
    serializer.close();
    final ProducerRecord<String, byte[]> record = new ProducerRecord<>(
        topic,
        null,
        ((ByteArrayOutputStream) outputStream).toByteArray()
    );
    producer.send(record);
    ((ByteArrayOutputStream) outputStream).reset();
    serializer.reset();
  }

  @Override
  public void close() throws Exception {
    super.close();
    producer.close();
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
