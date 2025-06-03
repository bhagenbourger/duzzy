# GCP GCS plugin

## Overview
Google Cloud Storage sink.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-gcp-gcs

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-gcp-gcs-${DUZZY_VERSION}-all.jar"
```

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.

Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.GcsSink ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.sink.GcsSink  
📋 Description: Sink data to Google Cloud Storage (GCS)  
📦 Module: io.duzzy.plugin-gcp-gcs  
🧬 Native support: true

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| project_id | projectId, project-id | The GCP project ID |  |
| credentials_file | credentialsFile, credentials-file | Path to the GCP credentials file |  |
| bucket_name | bucketName, bucket-name | The GCS bucket name |  |
| object_name | objectName, object-name | The GCS object name |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.GcsSink"
  project_id: "my-project-id"
  credentials_file: "/path/to/credentials.json"
  bucket_name: "my-bucket"
  object_name: "data/output.json"
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
```
