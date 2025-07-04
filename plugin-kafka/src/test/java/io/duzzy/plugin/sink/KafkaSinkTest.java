package io.duzzy.plugin.sink;

import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.serializer.XmlSerializer;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.kafka.KafkaContainer;

public class KafkaSinkTest {

  private static final KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

  @BeforeAll
  static void beforeAll() {
    kafka.start();
  }

  @AfterAll
  static void afterAll() {
    kafka.stop();
  }

  @Test
  void writeJsonMessage() throws Exception {
    final String topic = "json";

    final KafkaSink kafkaSink = new KafkaSink(
        new JsonSerializer(),
        topic,
        kafka.getBootstrapServers()
    );
    kafkaSink.init(null);
    kafkaSink.write(getDataOne());
    kafkaSink.write(getDataTwo());
    kafkaSink.close();

    try (
        final KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(getProperties("json"))
    ) {
      consumer.subscribe(Collections.singleton(topic));
      final ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofSeconds(10));
      final Iterator<ConsumerRecord<String, byte[]>> iterator = records.iterator();
      final String expected1 = "{\"c1\":1,\"c2\":\"one\"}";
      final String expected2 = "{\"c1\":2,\"c2\":\"two\"}";
      final int size = expected1.length() + expected2.length();

      assertThat(new String(iterator.next().value(), UTF_8)).isEqualTo(expected1);
      assertThat(new String(iterator.next().value(), UTF_8)).isEqualTo(expected2);
      assertThat(kafkaSink.getSerializer().size()).isEqualTo(size);
    }
  }

  @Test
  void writeXmlMessage() throws Exception {
    final String topic = "xml";

    final KafkaSink kafkaSink = new KafkaSink(
        new XmlSerializer(null, null),
        topic,
        kafka.getBootstrapServers()
    );
    kafkaSink.init(null);
    kafkaSink.write(getDataOne());
    kafkaSink.write(getDataTwo());
    kafkaSink.close();

    try (
        final KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(getProperties("xml"))
    ) {
      consumer.subscribe(Collections.singleton(topic));
      final ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofSeconds(10));
      final Iterator<ConsumerRecord<String, byte[]>> iterator = records.iterator();
      final String expected1 =
          "<?xml version='1.0' encoding='UTF-8'?><rows><row><c1>1</c1><c2>one</c2></row></rows>";
      final String expected2 =
          "<?xml version='1.0' encoding='UTF-8'?><rows><row><c1>2</c1><c2>two</c2></row></rows>";
      final int size = expected1.length() + expected2.length();
      assertThat(new String(iterator.next().value(), UTF_8)).isEqualTo(expected1);
      assertThat(new String(iterator.next().value(), UTF_8)).isEqualTo(expected2);
      assertThat(kafkaSink.getSerializer().size()).isEqualTo(size);
    }
  }

  private static @NotNull Properties getProperties(String groupId) {
    final Properties props = new Properties();
    props.put(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        kafka.getBootstrapServers()
    );
    props.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringDeserializer"
    );
    props.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.ByteArrayDeserializer"
    );
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return props;
  }
}
