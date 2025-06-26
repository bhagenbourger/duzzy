# Duckdb plugin

## Overview
Duckdb sink.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-duckdb

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-duckdb-${DUZZY_VERSION}-all.jar"
```

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.

Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.DuckdbSink ♨️
🔑 Identifier: io.duzzy.plugin.sink.DuckdbSink  
📋 Description: Sink data to a DuckDB database  
📦 Module: io.duzzy.plugin-duckdb  
🧬 Native support: false

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| url |  | The JDBC URL |  |
| user |  | The user |  |
| password |  | The password |  |
| fail_on_error | failOnError, fail-on-error | Whether to fail on error |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.DuckdbSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.SqlSerializer"
    table_name: "my_table"
  url: "jdbc:duckdb:/path/to/database"
  user: "user"
  password: "password"
  fail_on_error: true
```
