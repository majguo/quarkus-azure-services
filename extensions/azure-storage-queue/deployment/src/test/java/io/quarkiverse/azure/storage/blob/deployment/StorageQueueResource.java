package io.quarkiverse.azure.storage.blob.deployment;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueServiceClient;

@Path("/storagequeue")
public class StorageQueueResource {

    @Inject
    QueueServiceClient queueServiceClient;

    @GET
    public String test() {

        final QueueClient queue = queueServiceClient.getQueueClient("testqueue");

        queue.createIfNotExists();
        queue.clearMessages();
        queue.sendMessage("test-message");

        return queue
                .receiveMessage()
                .getBody()
                .toString();
    }
}
