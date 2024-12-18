#!/usr/bin/env bash
set -Euo pipefail

# The following environment variables need to be configured before running the script
# - RESOURCE_GROUP_NAME
# - STORAGE_ACCOUNT_NAME
# - APP_CONFIG_NAME
# - KEY_VAULT_NAME
# - COSMOSDB_ACCOUNT_NAME

# Azure Storage Blob Extension
# Authenticate to Azure Storage Blob with Microsoft Entra ID
# Export the endpoint of azure storage blob
export QUARKUS_AZURE_STORAGE_BLOB_ENDPOINT=$(az storage account show \
    --resource-group $RESOURCE_GROUP_NAME \
    --name $STORAGE_ACCOUNT_NAME \
    --query 'primaryEndpoints.blob' \
    --output tsv)
# Retrieve the storage account resource ID
STORAGE_ACCOUNT_RESOURCE_ID=$(az storage account show \
    --resource-group $RESOURCE_GROUP_NAME \
    --name $STORAGE_ACCOUNT_NAME \
    --query 'id' \
    --output tsv)
# Assign the "Storage Blob Data Contributor" role to the current signed-in identity
az role assignment create \
    --assignee $(az ad signed-in-user show --query 'id' --output tsv) \
    --role "Storage Blob Data Contributor" \
    --scope $STORAGE_ACCOUNT_RESOURCE_ID

# Azure App Configuration Extension
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

# Azure Key Vault Extension
export QUARKUS_AZURE_KEYVAULT_SECRET_ENDPOINT=$(az keyvault show --name "${KEY_VAULT_NAME}" \
    --resource-group "${RESOURCE_GROUP_NAME}" \
    --query properties.vaultUri\
    --output tsv)

# Azure Cosmos Extension
# Authenticate to Azure Cosmos DB with Microsoft Entra ID
# Export the endpoint of azure cosmos db
export QUARKUS_AZURE_COSMOS_ENDPOINT=$(az cosmosdb show \
    -n ${COSMOSDB_ACCOUNT_NAME} \
    -g ${RESOURCE_GROUP_NAME} \
    --query documentEndpoint -o tsv)
# Create a database and a container beforehand as data plane operations with assigned role cannot create them
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
# Assign the "Cosmos DB Data Contributor" role to the current signed-in identity
servicePrincipal=$(az ad sp list --filter "appId eq '$AZURE_CLIENT_ID'" --query '[0].id' -o tsv)
az cosmosdb sql role assignment create \
    --account-name ${COSMOSDB_ACCOUNT_NAME} \
    --resource-group ${RESOURCE_GROUP_NAME} \
    --scope "/" \
    --principal-id ${servicePrincipal} \
    --role-definition-id 00000000-0000-0000-0000-000000000002

# Run integration test with existing native executables against Azure services
mvn -B test-compile failsafe:integration-test -Dnative -Dazure.test=true

# Run both unit test and integration test in JVM mode against Azure services
mvn -B verify -Dazure.test=true

# Run integration test again for Azure Storage Blob Extension by using connection string for authentication
# Export the connection string that has full access to the account
export QUARKUS_AZURE_STORAGE_BLOB_CONNECTION_STRING=$(az storage account show-connection-string \
--resource-group "${RESOURCE_GROUP_NAME}" \
--name "${STORAGE_ACCOUNT_NAME}" \
--query connectionString -o tsv)
# Unset the endpoint environment variable
unset QUARKUS_AZURE_STORAGE_BLOB_ENDPOINT
cd azure-storage-blob
mvn -B test-compile failsafe:integration-test -Dnative -Dazure.test=true
mvn -B verify -Dazure.test=true

# Run integration test again for Azure Cosmos Extension by using key for authentication
# Export the key that has full access to the account including management plane and data plane operations
export QUARKUS_AZURE_COSMOS_KEY=$(az cosmosdb keys list \
    -n ${COSMOSDB_ACCOUNT_NAME} \
    -g ${RESOURCE_GROUP_NAME} \
    --query primaryMasterKey -o tsv)
cd ../azure-cosmos
mvn -B test-compile failsafe:integration-test -Dnative -Dazure.test=true
mvn -B verify -Dazure.test=true

# Change the directory back to integration-tests
cd ..
