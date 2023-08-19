/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.azure.storage.queue.it;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.models.QueueMessageItem;
import com.azure.storage.queue.models.QueueStorageException;

@Path("/quarkus-azure-storage-queue")
@ApplicationScoped
public class StorageQueueResource {

    @Inject
    QueueServiceClient queueServiceClient;

    @GET
    @Path("/{queueName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getQueueSize(@PathParam("queueName") String queueName) {

        QueueClient queue = queueServiceClient.getQueueClient(queueName);

        // receive a message and hide it for 30 seconds from others

        final int size;
        try {
            size = queue.getProperties().getApproximateMessagesCount();
        } catch (QueueStorageException e) {

            // performing operation on non-existing queue throws exception

            return Response.status(NOT_FOUND).build();
        }

        return Response.ok()
                .entity(Integer.toString(size))
                .build();
    }

    @POST
    @Path("/{queueName}/message")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendMessage(@PathParam("queueName") String queueName, String message) {

        QueueClient queue = queueServiceClient.getQueueClient(queueName);
        queue.createIfNotExists();

        queue.sendMessage(message);

        return Response.status(CREATED).build();
    }

    @GET
    @Path("/{queueName}/message")
    @Produces(MediaType.TEXT_PLAIN)
    public Response receiveMessage(@PathParam("queueName") String queueName) {

        QueueClient queue = queueServiceClient.getQueueClient(queueName);
        queue.createIfNotExists();

        // receive a message and hide it for 30 seconds from others

        QueueMessageItem message = queue.receiveMessage();

        if (message != null) {

            // delete received message so it does not pop-up for someone else after the hiding
            // period is over

            return Response.ok()
                    .entity(message.getBody().toString())
                    .build();

        }

        return Response.status(NOT_FOUND).build();
    }

    @PUT
    @Path("/{queueName}/clear")
    @Produces(MediaType.TEXT_PLAIN)
    public Response clearMessages(@PathParam("queueName") String queueName) {

        QueueClient queue = queueServiceClient.getQueueClient(queueName);
        queue.createIfNotExists();
        queue.clearMessages();

        return Response.ok().build();
    }

    @DELETE
    @Path("/{queueName}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteQueue(@PathParam("queueName") String queueName) {

        QueueClient queue = queueServiceClient.getQueueClient(queueName);
        queue.deleteIfExists();

        return Response.noContent().build();
    }
}
