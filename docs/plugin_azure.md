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
| account_name | accountName, account-name | The Azure Storage account name |  |
| create_container_if_not_exists | createContainerIfNotExists, create-container-if-not-exists | Whether to create the container if it does not exist | true |
| container |  | The name of the Azure Blob Storage container |  |
| blob_name | blobName, blob-name | The name of the blob to write data to |  |

### io.duzzy.plugin.sink.AzureDatalakeStorageSink ♨️
🔑 Identifier: io.duzzy.plugin.sink.AzureDatalakeStorageSink  
📋 Description: Sink data to a Azure Datalake Storage (ADLS) container  
📦 Module: io.duzzy.plugin-azure  
🧬 Native support: false

⚙️ Parameters:

| Name | Aliases | Description | Default value |
| --- | --- | --- | --- |
| serializer |  | The serializer to use |  |
| account_name | accountName, account-name | The Azure Storage account name |  |
| create_container_if_not_exists | createContainerIfNotExists, create-container-if-not-exists | Whether to create the container if it does not exist | true |
| container |  | The name of the Azure Blob Storage container |  |
| blob_name | blobName, blob-name | The name of the blob to write data to |  |