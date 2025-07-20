# GCP GCS plugin

## Overview
Google Cloud Storage sink.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-gcp

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-gcp-${DUZZY_VERSION}-all.jar"
```

### Authentication
One possible way to authenticate is to use a service account with a JSON key file. You can create a service account in the Google Cloud Console and download the key file. Then, you can specify the path to this key file in the `credentials_file` parameter of the sink configuration or use the `GOOGLE_APPLICATION_CREDENTIALS` environment variable to point to the key file.

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.

Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.GoogleCloudStorageSink ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.sink.GoogleCloudStorageSink  
📋 Description: Sink data to Google Cloud Storage (GCS)  
📦 Module: io.duzzy.plugin-gcp  
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
  identifier: "io.duzzy.plugin.sink.GoogleCloudStorageSink"
  project_id: "my-project-id"
  credentials_file: "/path/to/credentials.json"
  bucket_name: "my-bucket"
  object_name: "data/output.json"
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
```

### io.duzzy.plugin.sink.GooglePubsubSink ♨️ 🧬
🔑 Identifier: io.duzzy.plugin.sink.GooglePubsubSink  
📋 Description: Sink data to Google Cloud Pub/Sub  
📦 Module: io.duzzy.plugin-gcp  
🧬 Native support: true

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| project_id | projectId, project-id | The GCP project ID |  |
| topic_name | topicName, topic-name | The Pub/Sub topic name |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.GooglePubsubSink"
  project_id: "my-project-id"
  topic_name: "my-topic"
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
```
