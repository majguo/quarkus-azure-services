package io.quarkiverse.azure.services.together.it;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@EnabledIfSystemProperty(named = "azure.test", matches = "true")
@TestProfile(AppConfigurationConfigResourceWithLabelsTest.LabelsConfigurationProfile.class)
class AppConfigurationConfigResourceWithLabelsTest {
    public static class LabelsConfigurationProfile implements QuarkusTestProfile {
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.azure.app.configuration.labels", "prod");
        }
    }

    @Test
    void azureAppConfigurationSupportsLabels() {
        given()
                .get("/quarkus-services-app-configuration/config/{name}", "another.prop")
                .then()
                .statusCode(OK.getStatusCode())
                .body(equalTo("5678"));
    }

    @Test
    void configWithoutLabelIsNotProvided() {
        given()
                .get("/quarkus-services-app-configuration/config/{name}", "my.prop")
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }
}
