# Avro plugin

## Overview
Avro parser and avro serializers.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-avro

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-avro-${DUZZY_VERSION}-all.jar"
```

## Parser
A `parser`is a component that parses the input schema and produces a `DuzzySchema` by combining the input schema and duzzy config.

### io.duzzy.plugin.parser.AvroSchemaParser ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.parser.AvroSchemaParser  
📋 Description: Parse an Avro schema and produce a Duzzy schema  
📦 Module: io.duzzy.plugin-avro  
🧬 Native support: true

## Serializer
A serializer is a component that enables to specify how data are formatted.

### io.duzzy.plugin.serializer.AvroSchemalessSerializer ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.serializer.AvroSchemalessSerializer  
📋 Description: Serialize data to Avro, schema is not written with data  
📦 Module: io.duzzy.plugin-avro  
🧬 Native support: true

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| name |  | The name of the record |  |
| namespace |  | The namespace that qualifies the name |  |
| schema_file | schemaFile, schema-file | The Avro schema file |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.ConsoleSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.AvroSchemalessSerializer"
    name: "avro_name"
    namespace: "avro_namespace"
```

### io.duzzy.plugin.serializer.AvroWithSchemaSerializer ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.serializer.AvroWithSchemaSerializer  
📋 Description: Serialize data to Avro, schema is written with data  
📦 Module: io.duzzy.plugin-avro  
🧬 Native support: true

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| name |  | The name of the record |  |
| namespace |  | The namespace that qualifies the name |  |
| schema_file | schemaFile, schema-file | The Avro schema file |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.ConsoleSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.AvroWithSchemaSerializer"
    name: "avro_name"
    namespace: "avro_namespace"
```
