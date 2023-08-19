# Azure Blob Storage sample

This is a sample about implementing REST endpoints using the Quarkus Azure storage blob extension to upload and download
files to/from Azure Blob Storage.

## Prerequisites

To successfully run this sample, you need:

* JDK 11+ installed with JAVA_HOME configured appropriately
* Apache Maven 3.8.6+
* Azure CLI and Azure subscription if the specific Azure services are required
* Docker if you want to build the app as a native executable

You also need to make sure the right version of dependencies are installed.

### Use development iteration version

By default, the sample depends on the development iteration version, which is `999-SNAPSHOT`. To install the development
iteration version, you need to build it locally.

```
# Switch to the root directory of Quarkus Azure services extensions.
# For example, if you are in the directory of quarkus-azure-services/integration-tests/azure-storage-blob
cd ../..

# Install all Quarkus Azure services extensions locally.
mvn clean install -DskipTests

# Switch back to the directory of integration-tests/azure-storage-blob
cd integration-tests/azure-storage-blob
```

### Use release version

If you want to use the release version, you need to update the version of dependencies in the `pom.xml` file.

First, you need to find out the latest release version of the Quarkus Azure services extensions
from [releases](https://github.com/quarkiverse/quarkus-azure-services/releases), for example, `1.0.0`.

Then, update the version of dependencies in the `pom.xml` file, for example:

```xml

<parent>
    <groupId>io.quarkiverse.azureservices</groupId>
    <artifactId>quarkus-azure-services-parent</artifactId>
    <version>1.0.0</version>
    <relativePath></relativePath>
</parent>
```

## Preparing the Azure services

The Quarkus Azure storage blob extension supports **Dev Services**, it allows to run the sample without the real Azure
storage blob created and configured in the `dev` or `test` modes, you can go
to [Running the sample in development mode](#running-the-sample-in-development-mode) and have a try. However, if you
want to run the sample in JVM mode or as a native executable, you need to prepare the Azure storage blob first.

### Logging into Azure

Log into Azure and create a resource group for hosting the Azure storage blob to be created.

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
update [application.properties](src/main/resources/application.properties) by uncommenting the
same property and setting copied value.

## Running the sample

You have different choices to run the sample.

### Running the sample in development mode

First, you can launch the sample in `dev` mode.

```
mvn quarkus:dev
```

### Running the sample in JVM mode

You can also run the sample in JVM mode. Make sure you have
followed [Preparing the Azure services](#preparing-the-azure-services) to create the required Azure services.

```
# Build the package.
mvn package

# Run the generated jar file.
java -jar ./target/quarkus-app/quarkus-run.jar
```

### Running the sample as a native executable

You can even run the sample as a native executable. Make sure you have installed Docker and
followed [Preparing the Azure services](#preparing-the-azure-services) to create the required Azure services.

```
# Build the native executable using the Docker.
mvn package -Dnative -Dquarkus.native.container-build

# Run the native executable.
version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
./target/quarkus-azure-integration-test-storage-blob-${version}-runner
```

## Testing the sample

Open a new terminal and run the following commands to test the sample:

```
# Upload a blob with "Hello Quarkus Azure Storage Blob!" to Azure Blob Storage.
curl http://localhost:8080/quarkus-azure-storage-blob/testcontainer/testblob -X POST -d 'Hello Quarkus Azure Storage Blob!' -H "Content-Type: text/plain"

# Download the blob from Azure Blob Storage. You should see "Hello Quarkus Azure Storage Blob!" in the response.
curl http://localhost:8080/quarkus-azure-storage-blob/testcontainer/testblob -X GET

# Delete the blob from Azure Blob Storage.
curl http://localhost:8080/quarkus-azure-storage-blob/testcontainer/testblob -X DELETE

# Download the blob from Azure Blob Storage again. You should see "404 Not Found" in the response.
curl http://localhost:8080/quarkus-azure-storage-blob/testcontainer/testblob -X GET -I
```

Press `Ctrl + C` to stop the sample once you complete the try and test.

## Cleaning up Azure resources

Run the following command to clean up the Azure resources if you created before:

```
az group delete \
    --name ${RESOURCE_GROUP_NAME} \
    --yes --no-wait
```
