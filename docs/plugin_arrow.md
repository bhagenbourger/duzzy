# Arrow plugin

## Overview
Arrow serializer.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-arrow

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-arrow-${DUZZY_VERSION}-all.jar"
```

## Serializer
A serializer is a component that enables to specify how data are formatted.

### io.duzzy.plugin.serializer.ArrowSerializer ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.serializer.ArrowSerializer  
📋 Description: Serialize data to Apache Arrow format  
📦 Module: io.duzzy.plugin-arrow  
🧬 Native support: true

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| name |  | The name of the record |  |
| namespace |  | The namespace that qualifies the name |  |
| schema_file | schemaFile, schema-file | The Avro schema file |  |
| batch_size | batchSize, batch-size | The size of each batch written to the output stream |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.ConsoleSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.ArrowSerializer"
    name: "arrow_name"
    namespace: "arrow_namespace"
    schema_file: "/path/to/schema.avsc"
    batch_size: 1000
```
