# Duckdb plugin
Duckdb sink.

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.  
Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.DuckdbSink
Identifier: io.duzzy.plugin.sink.DuckdbSink  
Description: Sink data to a DuckDB database

Parameters:
- Name: serializer

  Description: The serializer to use
- Name: url

  Description: The JDBC URL
- Name: user

  Description: The user
- Name: password

  Description: The password
- Name: fail_on_error

  Aliases: failOnError, fail-on-error

  Description: Whether to fail on error

Example:
```
---
identifier: "io.duzzy.plugin.sink.DuckdbSink"
serializer:
  identifier: "io.duzzy.plugin.serializer.SqlSerializer"
  table_name: "my_table"
url: "jdbc:duckdb:file:/path/to/database"
user: "user"
password: "password"
fail_on_error: true
```