package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.OutputStreamSink;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaSink extends OutputStreamSink {

  private final String topic;
  private final String bootstrapServers;
  private final KafkaProducer<String, byte[]> producer;

  @JsonCreator
  public KafkaSink(
      @JsonProperty("serializer")
      Serializer<?, OutputStream> serializer,
      @JsonProperty("topic")
      String topic,
      @JsonProperty("bootstrap_servers")
      @JsonAlias({"bootstrapServers", "bootstrap-servers"})
      String bootstrapServers
  ) {
    super(serializer);
    this.topic = topic;
    this.bootstrapServers = bootstrapServers;
    this.producer = new KafkaProducer<>(buildProperties(bootstrapServers));
  }

  @Override
  public OutputStream outputSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  public void write(DuzzyRow row) throws Exception {
    reset();
    super.write(row);
    serializer.close();
    sendToKafka();
  }

  @Override
  public void close() throws Exception {
    super.close();
    producer.close();
  }

  @Override
  public KafkaSink fork(Long threadId) throws Exception {
    return new KafkaSink(serializer.fork(threadId), topic, bootstrapServers);
  }

  private void reset() throws IOException {
    if (((ByteArrayOutputStream) getOutput()).size() > 0) {
      ((ByteArrayOutputStream) getOutput()).reset();
      serializer.reset();
    }
  }

  private void sendToKafka() throws IOException {
    final ProducerRecord<String, byte[]> record = new ProducerRecord<>(
        topic,
        null,
        ((ByteArrayOutputStream) getOutput()).toByteArray()
    );
    producer.send(record);
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
