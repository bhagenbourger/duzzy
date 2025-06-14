# Parquet plugin

## Overview
Parquet serializer.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-parquet

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-parquet-${DUZZY_VERSION}-all.jar"
```

## Serializer
A serializer is a component that enables to specify how data are formatted.

### io.duzzy.plugin.serializer.ParquetSerializer ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.serializer.ParquetSerializer  
📋 Description: Serialize data to Parquet  
📦 Module: io.duzzy.plugin-parquet  
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
    identifier: "io.duzzy.plugin.serializer.ParquetSerializer"
    name: "parquet"
    namespace: "io.duzzy.plugin.serializer"
    schema_file: "schema.avsc"
```
