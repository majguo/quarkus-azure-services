# Quarkus Azure Services - Integration Tests

This is the integration test for testing all Quarkus Azure services extensions from REST endpoints. You can also find the same AZ CLI commands to create Azure services in `.github/build-with-maven-native.sh`.

## Prerequisites

To successfully run the integration test, you need:

* JDK 17+ installed with JAVA_HOME configured appropriately
* Apache Maven 3.8.6+
* Azure CLI and Azure subscription
* Docker

You also need to clone the repository and switch to the directory of integration tests.

```
git clone https://github.com/quarkiverse/quarkus-azure-services.git
cd quarkus-azure-services/integration-tests
```

## Installing dependencies locally in development iteration

The version of Quarkus Azure services extensions in development iteration is `999-SNAPSHOT`, which is not available
from Maven Central, you need to install them locally before running the test.

```
mvn clean install -DskipTests --file ../pom.xml
```

## Running the test with Dev services

Some Quarkus Azure services extensions support Dev Services, it allows to test easily.
Launch these tests with:

```
mvn test
```

## Running the test with Azure services

If you want to run all the test with real Azure services in the native mode, you need to create the dependent services
on Azure after logging into Azure.

### Logging into Azure

Log into Azure and create a resource group for hosting different Azure services to be created.

```
az login

RESOURCE_GROUP_NAME=<resource-group-name>
az group create \
    --name ${RESOURCE_GROUP_NAME} \
    --location centralus
```

### Creating Azure Storage Account

Run the following commands to create an Azure Storage Account and export its connection string as an environment
variable.

```
STORAGE_ACCOUNT_NAME=<unique-storage-account-name>
az storage account create \
    --name ${STORAGE_ACCOUNT_NAME} \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --location centralus \
    --sku Standard_LRS \
    --kind StorageV2

export QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING=$(az storage account show-connection-string \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --name ${STORAGE_ACCOUNT_NAME} \
    --query connectionString -o tsv)
echo "The value of 'quarkus.azure.storage.blob.connection-string' is: ${QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING}"
```

The value of environment variable `QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING` will be fed into config
property `quarkus.azure.storage.blob.connection-string` of `azure-storage-blob` extension in order to set up the
connection to the Azure Storage Account.

You can also manually copy the output of the variable `quarkus.azure.storage.blob.connection-string` and then
update [application.properties](azure-storage-blob/src/main/resources/application.properties) by uncommenting the
same property and setting copied value.

### Creating Azure App Configuration

Run the following commands to create an Azure App Configuration store, add a few key-value pairs, and export its
connection info as environment variables.

```
APP_CONFIG_NAME=<unique-app-config-name>
az appconfig create \
    --name "${APP_CONFIG_NAME}" \
    --resource-group "${RESOURCE_GROUP_NAME}" \
    --location centralus

az appconfig kv set \
    --name "${APP_CONFIG_NAME}" \
    --key my.prop \
    --value 1234 \
    --yes
az appconfig kv set \
    --name "${APP_CONFIG_NAME}" \
    --key another.prop \
    --value 5678 \
    --label prod \
    --yes
    
export QUARKUS_AZURE_APP_CONFIGURATION_ENDPOINT=$(az appconfig show \
  --resource-group "${RESOURCE_GROUP_NAME}" \
  --name "${APP_CONFIG_NAME}" \
  --query endpoint -o tsv)
credential=$(az appconfig credential list \
    --name "${APP_CONFIG_NAME}" \
    --resource-group "${RESOURCE_GROUP_NAME}" \
    | jq 'map(select(.readOnly == true)) | .[0]')
export QUARKUS_AZURE_APP_CONFIGURATION_ID=$(echo "${credential}" | jq -r '.id')
export QUARKUS_AZURE_APP_CONFIGURATION_SECRET=$(echo "${credential}" | jq -r '.value')
```

The values of environment
variable `QUARKUS_AZURE_APP_CONFIGURATION_ENDPOINT` / `QUARKUS_AZURE_APP_CONFIGURATION_ID` / `QUARKUS_AZURE_APP_CONFIGURATION_SECRET`
will be fed into config
properties `quarkus.azure.app.configuration.endpoint` / `quarkus.azure.app.configuration.id` / `quarkus.azure.app.configuration.secret`
of `azure-app-configuration` extension in order to set up the connection to the Azure App Configuration store.

### Creating Azure Key Vault

Run the following commands to create an Azure Key Vault, set permission and export its connection string as an environment
variable.  

```
KEY_VAULT_NAME=<unique-key-vault-name>
az keyvault create --name ${KEY_VAULT_NAME} \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --location eastus \
    --enable-rbac-authorization false

export QUARKUS_AZURE_KEYVAULT_SECRET_ENDPOINT=$(az keyvault show --name ${KEY_VAULT_NAME}\
    --resource-group ${RESOURCE_GROUP_NAME}\
    --query properties.vaultUri -otsv)
echo "The value of 'quarkus.azure.keyvault.secret.endpoint' is: ${QUARKUS_AZURE_KEYVAULT_SECRET_ENDPOINT}"
```

Add secret `secret1` with value `mysecret`.

```
az keyvault secret set \
    --vault-name ${KEY_VAULT_NAME} \
    --name secret1 \
    --value mysecret
```

The value of environment variable `QUARKUS_AZURE_KEYVAULT_SECRET_ENDPOINT` will be fed into config
property `quarkus.azure.keyvault.secret.endpoint` of `azure-keyvault` extension in order to set up the
connection to the Azure Key Vault.

### Creating Azure Azure Cosmos DB account

Run the following commands to create an Azure Cosmos DB account, and export its endpoint as an environment variable.

```
COSMOSDB_ACCOUNT_NAME=<unique-cosmosdb-account-name>
az cosmosdb create \
    -n ${COSMOSDB_ACCOUNT_NAME} \
    -g ${RESOURCE_GROUP_NAME} \
    --default-consistency-level Session \
    --locations regionName='West US' failoverPriority=0 isZoneRedundant=False

export QUARKUS_AZURE_COSMOS_ENDPOINT=$(az cosmosdb show \
    -n ${COSMOSDB_ACCOUNT_NAME} \
    -g ${RESOURCE_GROUP_NAME} \
    --query documentEndpoint -o tsv)
echo "The value of 'quarkus.azure.cosmos.endpoint' is: ${QUARKUS_AZURE_COSMOS_ENDPOINT}"
```

The value of environment variable `QUARKUS_AZURE_COSMOS_ENDPOINT` will be fed into config
property `quarkus.azure.cosmos.endpoint` of `azure-cosmos` extension in order to set up the
connection to the Azure Cosmos DB.

Assign the `Cosmos DB Built-in Data Contributor` role to the signed-in user, so that the sample application can do data plane CRUD operations.

```
az ad signed-in-user show --query id -o tsv \
    | az cosmosdb sql role assignment create \
    --account-name ${COSMOSDB_ACCOUNT_NAME} \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --scope "/" \
    --principal-id @- \
    --role-definition-id 00000000-0000-0000-0000-000000000002
```

However, you cannot use any Azure Cosmos DB data plane SDK to authenticate management operations with a Microsoft Entra identity, so you need to create database and container manually.
The following commands create a database `demodb` and a container `democontainer` using Azure CLI.

```
az cosmosdb sql database create \
    -a ${COSMOSDB_ACCOUNT_NAME} \
    -g ${RESOURCE_GROUP_NAME} \
    -n demodb
az cosmosdb sql container create \
    -a ${COSMOSDB_ACCOUNT_NAME} \
    -g ${RESOURCE_GROUP_NAME} \
    -d demodb \
    -n democontainer \
    -p "/id"
```

### Creating Azure Event Hubs resources

Run the following commands to create an Azure eventhub namespace and an eventhub within the namespace, and export the environment variables to be used in the sample application.

```
EVENTHUBS_NAMESPACE=<unique-eventhub-namespace-name>
EVENTHUBS_EVENTHUB_NAME=<unique-eventhub-name>
# Azure Event Hubs Extension
az eventhubs namespace create \
    --name ${EVENTHUBS_NAMESPACE} \
    --resource-group ${RESOURCE_GROUP_NAME}
az eventhubs eventhub create \
    --name ${EVENTHUBS_EVENTHUB_NAME} \
    --namespace-name ${EVENTHUBS_NAMESPACE} \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --partition-count 2

export QUARKUS_AZURE_EVENTHUBS_NAMESPACE=${EVENTHUBS_NAMESPACE}
export QUARKUS_AZURE_EVENTHUBS_EVENTHUB_NAME=${EVENTHUBS_EVENTHUB_NAME}
```

Assign the `Azure Event Hubs Data Owner` role to the signed-in user as a Microsoft Entra identity, so that the sample application can do data plane operations.

```
EVENTHUBS_EVENTHUB_RESOURCE_ID=$(az eventhubs eventhub show \
    --resource-group $RESOURCE_GROUP_NAME \
    --namespace-name $EVENTHUBS_NAMESPACE \
    --name $EVENTHUBS_EVENTHUB_NAME \
    --query 'id' \
    --output tsv)
az role assignment create \
    --role "Azure Event Hubs Data Owner" \
    --assignee $(az ad signed-in-user show --query 'id' --output tsv) \
    --scope $EVENTHUBS_EVENTHUB_RESOURCE_ID
```

### Running the test

Finally, build the native executable and launch the test with:

```
mvn integration-test -Dnative -Dquarkus.native.container-build -Dnative.surefire.skip -Dazure.test=true
```

### Cleaning up Azure resources

Once you complete the test, run the following command to clean up the Azure resources used in the test:

```
az group delete \
    --name ${RESOURCE_GROUP_NAME} \
    --yes --no-wait
```
