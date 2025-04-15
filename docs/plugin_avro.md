# Avro plugin
Avro parser and avro serializers.

## Parser
A `parser`is a component that parses the input schema and produces a `DuzzySchema` by combining the input schema and duzzy config.

### io.duzzy.plugin.parser.AvroSchemaParser
Identifier: io.duzzy.plugin.parser.AvroSchemaParser  
Description: Parse an Avro schema and produce a Duzzy schema

## Serializer
A serializer is a component that enables to specify how data are formatted.

### io.duzzy.plugin.serializer.AvroWithSchemaSerializer
Identifier: io.duzzy.plugin.serializer.AvroWithSchemaSerializer  
Description: Serialize data to Avro, schema is written with data

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
identifier: "io.duzzy.plugin.serializer.AvroWithSchemaSerializer"
name: "avro-with-schema"
namespace: "io.duzzy.plugin.serializer"
schema_file: "schema.avsc"
```

### io.duzzy.plugin.serializer.AvroSchemalessSerializer
Identifier: io.duzzy.plugin.serializer.AvroSchemalessSerializer  
Description: Serialize data to Avro, schema is not written with data

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
identifier: "io.duzzy.plugin.serializer.AvroSchemalessSerializer"
name: "avro-schemaless"
namespace: "io.duzzy.plugin.serializer"
schema_file: "schema.avsc"
```