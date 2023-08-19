# Quarkus Azure Services - Integration Tests

This is the integration test for testing all Quarkus Azure services extensions from REST endpoints.

## Installing dependencies locally in development iteration

The version of Quarkus Azure services extensions in development iteration is `999-SNAPSHOT`, which is not available
from Maven Central, you need to install them locally before running the test.

```
# Switch to the root directory of Quarkus Azure services extensions.
# For example, if you are in the directory of quarkus-azure-services/integration-tests
cd ..

# Install all Quarkus Azure services extensions locally.
mvn clean install -DskipTests

# Switch back to the directory of integration-tests
cd integration-tests
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
    --location eastus
```

### Creating Azure Storage Account

Run the following commands to create an Azure Storage Account and export its connection string as an environment
variable.

```
STORAGE_ACCOUNT_NAME=<unique-storage-account-name>
az storage account create \
    --name ${STORAGE_ACCOUNT_NAME} \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --location eastus \
    --sku Standard_LRS \
    --kind StorageV2

export QUARKUS_AZURE_STORAGE_CONNECTION_STRING=$(az storage account show-connection-string \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --name ${STORAGE_ACCOUNT_NAME} \
    --query connectionString -o tsv)

export QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING=${QUARKUS_AZURE_STORAGE_CONNECTION_STRING}"
export QUARKUS_AZURE_STORAGE_QUEUE_CONNECTION_STRING=${QUARKUS_AZURE_STORAGE_CONNECTION_STRING}"

echo "The value of 'quarkus.azure.storage.blob.connection-string' is: ${QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING}"
echo "The value of 'quarkus.azure.storage.queue.connection-string' is: ${QUARKUS_AZURE_STORAGE_QUEUE_CONNECTION_STRING}"
```

The value of environment variables:
* `QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING`, and
* `QUARKUS_AZURE_STORAGE_QUEUE_CONNECTION_STRING`
Will be fed into configuration strings:
* `quarkus.azure.storage.blob.connection-string`, and
* `quarkus.azure.storage.queue.connection-string`
Of `azure-storage-blob` extension in order to set up the connection to the Azure Storage Account.

Please note that in general these connections strings are identical, but first
one is for the blob extension, and the second one is for the queue extension.

You can also manually copy the output of the variable `quarkus.azure.storage.blob.connection-string`
and then update [application.properties](azure-storage-blob/src/main/resources/application.properties) by uncommenting the same property and setting copied value.

### Creating Azure App Configuration

Run the following commands to create an Azure App Configuration store, add a few key-value pairs, and export its
connection info as environment variables.

```
export APP_CONFIG_NAME=<unique-app-config-name>
az appconfig create \
    --name "${APP_CONFIG_NAME}" \
    --resource-group "${RESOURCE_GROUP_NAME}" \
    --location eastus

az appconfig kv set \
    --name "${APP_CONFIG_NAME}" \
    --key my.prop \
    --value 1234 \
    --yes
az appconfig kv set \
    --name "${APP_CONFIG_NAME}" \
    --key another.prop \
    --value 5678 \
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
