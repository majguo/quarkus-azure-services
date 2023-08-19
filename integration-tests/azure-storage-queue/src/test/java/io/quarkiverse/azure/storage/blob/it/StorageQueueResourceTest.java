package io.quarkiverse.azure.storage.blob.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StorageQueueResourceTest {

    @Test
    void shouldUploadATextfile() {

        final String queueName = "container-quarkus-azure-storage-queue";
        final String body = "Hello quarkus-azure-storage-queue at " + LocalDateTime.now();

        try {

            // post message

            given()
                    .when()
                    .body(body)
                    .post("/quarkus-azure-storage-queue/" + queueName + "/message")
                    .then()
                    .statusCode(201);

            // get approximate messages count - should be 1

            given()
                    .when()
                    .get("/quarkus-azure-storage-queue/" + queueName)
                    .then()
                    .statusCode(200)
                    .body(is("1"));

            // read message

            given()
                    .when()
                    .get("/quarkus-azure-storage-queue/" + queueName + "/message")
                    .then()
                    .statusCode(200)
                    .body(is(body));

            // read another message - this should return 404 since there is no new messages

            given()
                    .when()
                    .get("/quarkus-azure-storage-queue/" + queueName + "/message")
                    .then()
                    .statusCode(404);

            // post message again

            given()
                    .when()
                    .body(body)
                    .post("/quarkus-azure-storage-queue/" + queueName + "/message")
                    .then()
                    .statusCode(201);

            // clear messages in the queue

            given()
                    .when()
                    .put("/quarkus-azure-storage-queue/" + queueName + "/clear")
                    .then()
                    .statusCode(200);

            // read message - this should return 404 since we cleared the queue

            given()
                    .when()
                    .get("/quarkus-azure-storage-queue/" + queueName + "/message")
                    .then()
                    .statusCode(404);

        } finally {

            // delete queue

            given()
                    .when()
                    .delete("/quarkus-azure-storage-queue/" + queueName)
                    .then()
                    .statusCode(204);

            // when trying to get messages count we should get error 404 since queue does not
            // exist anymore

            given()
                    .when()
                    .get("/quarkus-azure-storage-queue/" + queueName)
                    .then()
                    .statusCode(404);
        }
    }
}
