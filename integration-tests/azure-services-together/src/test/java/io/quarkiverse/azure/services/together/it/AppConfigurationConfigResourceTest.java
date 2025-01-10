package io.quarkiverse.azure.services.together.it;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@EnabledIfSystemProperty(named = "azure.test", matches = "true")
class AppConfigurationConfigResourceTest {
    @Test
    void azureAppConfigurationConfig() {
        given()
                .get("/quarkus-services-app-configuration/config/{name}", "my.prop")
                .then()
                .statusCode(OK.getStatusCode())
                .body(equalTo("1234"));

        given()
                .get("/quarkus-services-app-configuration/config/{name}", "another.prop")
                .then()
                .statusCode(OK.getStatusCode())
                .body(equalTo("5678"));
    }
}
