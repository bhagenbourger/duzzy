package io.duzzy.plugin.sink;

import static io.duzzy.tests.Data.getDataOneWithKey;
import static io.duzzy.tests.Data.getDataTwoWithKey;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.plugin.serializer.JsonSerializer;
import io.duzzy.plugin.serializer.XmlSerializer;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
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
        kafka.getBootstrapServers(),
        ByteArraySerializer.class.getName(),
        StringSerializer.class.getName(),
        null
    );
    kafkaSink.init(null);
    kafkaSink.write(getDataOneWithKey());
    kafkaSink.write(getDataTwoWithKey());
    kafkaSink.close();

    try (
        final KafkaConsumer<byte[], String> consumer = new KafkaConsumer<>(getProperties(
            "json",
            ByteArrayDeserializer.class.getName(),
            StringDeserializer.class.getName()
        ))
    ) {
      consumer.subscribe(Collections.singleton(topic));
      final ConsumerRecords<byte[], String> records = consumer.poll(Duration.ofSeconds(10));
      final Iterator<ConsumerRecord<byte[], String>> iterator = records.iterator();
      final String expected1 = "{\"c1\":1,\"c2\":\"one\"}";
      final String expected2 = "{\"c1\":2,\"c2\":\"two\"}";
      final int size = expected1.length() + expected2.length();

      final ConsumerRecord<byte[], String> first = iterator.next();
      assertThat(new String(first.key(), UTF_8)).isEqualTo("key1");
      assertThat(first.value()).isEqualTo(expected1);
      final ConsumerRecord<byte[], String> second = iterator.next();
      assertThat(new String(second.key(), UTF_8)).isEqualTo("key2");
      assertThat(second.value()).isEqualTo(expected2);
      assertThat(kafkaSink.size()).isEqualTo(size);
    }
  }

  @Test
  void writeXmlMessage() throws Exception {
    final String topic = "xml";

    final KafkaSink kafkaSink = new KafkaSink(
        new XmlSerializer(null, null),
        topic,
        kafka.getBootstrapServers(),
        null,
        null,
        null
    );
    kafkaSink.init(null);
    kafkaSink.write(getDataOneWithKey());
    kafkaSink.write(getDataTwoWithKey());
    kafkaSink.close();

    try (
        final KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(getProperties(
            "xml",
            StringDeserializer.class.getName(),
            ByteArrayDeserializer.class.getName()
        ))
    ) {
      consumer.subscribe(Collections.singleton(topic));
      final ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofSeconds(10));
      final Iterator<ConsumerRecord<String, byte[]>> iterator = records.iterator();
      final String expected1 =
          "<?xml version='1.0' encoding='UTF-8'?><rows><row><c1>1</c1><c2>one</c2></row></rows>";
      final String expected2 =
          "<?xml version='1.0' encoding='UTF-8'?><rows><row><c1>2</c1><c2>two</c2></row></rows>";
      final int size = expected1.length() + expected2.length();
      final ConsumerRecord<String, byte[]> first = iterator.next();
      assertThat(first.key()).isEqualTo("key1");
      assertThat(new String(first.value(), UTF_8)).isEqualTo(expected1);
      final ConsumerRecord<String, byte[]> second = iterator.next();
      assertThat(second.key()).isEqualTo("key2");
      assertThat(new String(second.value(), UTF_8)).isEqualTo(expected2);
      assertThat(kafkaSink.size()).isEqualTo(size);
    }
  }

  private static @NotNull Properties getProperties(
      String groupId,
      String keyDeserializer,
      String valueDeserializer
  ) {
    final Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
    props.put(GROUP_ID_CONFIG, groupId);
    props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    return props;
  }
}
