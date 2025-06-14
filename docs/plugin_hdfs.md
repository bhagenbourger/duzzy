# HDFS plugin

## Overview
Hdfs sink.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-hdfs

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-hdfs-${DUZZY_VERSION}-all.jar"
```

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.

Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.HdfsSink ♨️
🔑 Identifier: io.duzzy.plugin.sink.HdfsSink  
📋 Description: Sink data to HDFS  
📦 Module: io.duzzy.plugin-hdfs  
🧬 Native support: false

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| core_site_file | coreSitePath, core-site-file | The core-site.xml file |  |
| hdfs_site_path | hdfsSiteFile, hdfs-site-file | The hdfs-site.xml file |  |
| filename |  | The filename to write to |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.HdfsSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.CSVSerializer"
  coreSiteFile: "/path/to/core-site.xml"
  hdfsSitePath: "/path/to/hdfs-site.xml"
  filename: "/path/to/file"
```
