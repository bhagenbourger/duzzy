package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.column.Column;
import io.duzzy.core.sink.Sink;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class KafkaSink extends Sink {

    private final String topic;
    private final KafkaProducer<String, byte[]> producer;

    @JsonCreator
    public KafkaSink(
            @JsonProperty("serializer") Serializer<?> serializer,
            @JsonProperty("topic") String topic,
            @JsonProperty("bootstrapServers") String bootstrapServers
    ) {
        super(serializer);
        this.topic = topic;
        this.producer = new KafkaProducer<>(buildProperties(bootstrapServers));
    }

    @Override
    public void init(DuzzySchema duzzySchema) {

    }

    @Override
    public void write(DataItems data) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        serializer.writeUnit(data, outputStream);
        final ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                topic,
                null,
                outputStream.toByteArray()
        );
        producer.send(record);
    }

    @Override
    public void close() throws IOException {
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
