package io.quarkiverse.azure.storage.queue.runtime;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import com.azure.core.util.ClientOptions;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.QueueServiceClientBuilder;

import io.quarkiverse.azure.core.util.AzureQuarkusIdentifier;

public class StorageQueueServiceClientProducer {

    @Inject
    StorageQueueConfig storageQueueConfiguration;

    @Produces
    public QueueServiceClient blobServiceClient() {
        return new QueueServiceClientBuilder()
                .clientOptions(new ClientOptions().setApplicationId(AzureQuarkusIdentifier.AZURE_QUARKUS_STORAGE_BLOB))
                .connectionString(storageQueueConfiguration.connectionString)
                .buildClient();
    }
}
