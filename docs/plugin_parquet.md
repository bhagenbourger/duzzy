# Parquet plugin
Parquet serializer.

## Serializer
A serializer is a component that enables to specify how data are formatted.

### io.duzzy.plugin.serializer.ParquetSerializer
Identifier: io.duzzy.plugin.serializer.ParquetSerializer  
Description: Serialize data to Parquet

Parameters:
- Name: name

  Description: The name of the record
- Name: namespace

  Description: The namespace that qualifies the name
- Name: schema_file

  Aliases: schemaFile, schema-file

  Description: The Avro schema file

Example:
```
---
identifier: "io.duzzy.plugin.serializer.ParquetSerializer"
name: "parquet"
namespace: "io.duzzy.plugin.serializer"
schema_file: "schema.avsc"
```