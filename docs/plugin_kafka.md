# Kafka plugin
Kafka sink.

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.  
Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.KafkaSink
Identifier: io.duzzy.plugin.sink.KafkaSink  
Description: Sink data to a Kafka topic

Parameters:
- Name: serializer

  Description: The serializer to use
- Name: topic

  Description: The Kafka topic
- Name: bootstrap_servers

  Aliases: bootstrapServers, bootstrap-servers

  Description: The Kafka bootstrap servers

Example:
```
---
identifier: "io.duzzy.plugin.sink.KafkaSink"
serializer:
  identifier: "io.duzzy.plugin.serializer.JSONSerializer"
topic: "my-topic"
bootstrapServers: "localhost:9092"
```