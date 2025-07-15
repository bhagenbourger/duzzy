# Azure plugin

## Overview
Azure sink.

### Plugin information
groupId: io.duzzy  
artifactId: plugin-azure

### Installation
```
duzzy plugin install --source "https://github.com/bhagenbourger/duzzy/releases/download/v${DUZZY_VERSION}/plugin-plugin-azure-${DUZZY_VERSION}-all.jar"
```

### Authentication
Azure plugin uses the Azure SDK for Java, which supports various authentication methods. One possible way to authenticate is to use azure CLI. You can log in using the Azure CLI with the command `az login`, which will authenticate your session and allow the plugin to access Azure resources.

## Sink
A sink is a component that enables to specify where and in which format (via a serializer) data are written.

Before writing data, a sink delegates data formatting to a serializer.

### io.duzzy.plugin.sink.AzureBlobStorageSink ♨️
🔑 Identifier: io.duzzy.plugin.sink.AzureBlobStorageSink  
📋 Description: Sink data to a Azure Blob Storage container  
📦 Module: io.duzzy.plugin-azure  
🧬 Native support: false

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| azure_auth_type | azureAuthType, azure-auth-type | The type of authentication to use for Azure Blob Storage. Options are 'DEFAULT_AZURE_CREDENTIALS' (default) or 'CONNECTION_STRING'. If 'CONNECTION_STRING' is used, the connection string must be set in the environment variable AZURE_STORAGE_CONNECTION_STRING. | DEFAULT_AZURE_CREDENTIALS |
| account_name | accountName, account-name | The Azure Storage account name |  |
| service_version | serviceVersion, service-version | The Azure Blob Storage service version to use. If not specified, the latest version will be used. | LATEST |
| create_container_if_not_exists | createContainerIfNotExists, create-container-if-not-exists | Whether to create the container if it does not exist | true |
| container |  | The name of the Azure Blob Storage container |  |
| blob_name | blobName, blob-name | The name of the blob to write data to |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.AzureBlobStorageSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
  azureAuthType: "DEFAULT_AZURE_CREDENTIALS"
  accountName: "myaccount"
  createContainerIfNotExists: true
  container: "mycontainer"
  blobName: "myblob.txt"
```

### io.duzzy.plugin.sink.AzureDatalakeStorageSink ♨️
🔑 Identifier: io.duzzy.plugin.sink.AzureDatalakeStorageSink  
📋 Description: Sink data to a Azure Datalake Storage (ADLS) container  
📦 Module: io.duzzy.plugin-azure  
🧬 Native support: false

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| azure_auth_type | azureAuthType, azure-auth-type | The type of authentication to use for Azure Blob Storage. Options are 'DEFAULT_AZURE_CREDENTIALS' (default) or 'CONNECTION_STRING'. If 'CONNECTION_STRING' is used, the connection string must be set in the environment variable AZURE_STORAGE_CONNECTION_STRING. | DEFAULT_AZURE_CREDENTIALS |
| account_name | accountName, account-name | The Azure Storage account name |  |
| service_version | serviceVersion, service-version | The Azure Blob Storage service version to use. If not specified, the latest version will be used. | LATEST |
| create_container_if_not_exists | createContainerIfNotExists, create-container-if-not-exists | Whether to create the container if it does not exist | true |
| container |  | The name of the Azure Blob Storage container |  |
| blob_name | blobName, blob-name | The name of the blob to write data to |  |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.AzureDatalakeStorageSink"
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
  azureAuthType: "DEFAULT_AZURE_CREDENTIALS"
  accountName: "myaccount"
  createContainerIfNotExists: true
  container: "mycontainer"
  blobName: "myblob.txt"
```

### io.duzzy.plugin.sink.AzureEventHubSink ♨️
🔑 Identifier: io.duzzy.plugin.sink.AzureEventHubSink  
📋 Description: Sink data to an Azure Event Hub  
📦 Module: io.duzzy.plugin-azure  
🧬 Native support: false

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| azure_auth_type | azureAuthType, azure-auth-type | The type of authentication to use for Azure Event Hub. Options are 'DEFAULT_AZURE_CREDENTIALS' (default) or 'CONNECTION_STRING'. If 'CONNECTION_STRING' is used, the connection string must be set in the environment variable AZURE_EVENT_HUB_CONNECTION_STRING. | DEFAULT_AZURE_CREDENTIALS |
| event_hub_name | eventHubName, event-hub-name | The name of the Azure Event Hub, required only if azure_auth_type is DEFAULT_AZURE_CREDENTIALS. |  |
| fully_qualified_namespace | fullyQualifiedNamespace, fully-qualified-namespace | The fully qualified namespace of the Azure Event Hub, required only if azure_auth_type is DEFAULT_AZURE_CREDENTIALS. |  |
| fail_on_error | failOnError, fail-on-error | Whether to fail on error when sending events. If true, an IOException will be thrown if an event cannot be sent. | true |  

💡 Example:
```
---
sink:
  identifier: "io.duzzy.plugin.sink.AzureEventHubSink"
  azure_auth_type: "DEFAULT_AZURE_CREDENTIALS"
  event_hub_name: "my-event-hub"
  fully_qualified_namespace: "my-namespace.servicebus.windows.net"
  fail_on_error: true
  serializer:
    identifier: "io.duzzy.plugin.serializer.JsonSerializer"
```