# Kafka plugin

## Overview
Kafka sink.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-kafka

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-kafka-${DUZZY_VERSION}-all.jar"
```

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.

Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.KafkaSink ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.sink.KafkaSink  
📋 Description: Sink data to a Kafka topic  
📦 Module: io.duzzy.plugin-kafka  
🧬 Native support: true

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| topic |  | The Kafka topic |  |
| bootstrap_servers | bootstrapServers, bootstrap-servers | The Kafka bootstrap servers |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.KafkaSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
  topic: "my-topic"
  bootstrapServers: "localhost:9092"
```
