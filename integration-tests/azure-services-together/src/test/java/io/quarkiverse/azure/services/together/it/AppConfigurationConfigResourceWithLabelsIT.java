package io.quarkiverse.azure.services.together.it;

import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@EnabledIfSystemProperty(named = "azure.test", matches = "true")
@TestProfile(AppConfigurationConfigResourceWithLabelsTest.LabelsConfigurationProfile.class)
public class AppConfigurationConfigResourceWithLabelsIT extends AppConfigurationConfigResourceWithLabelsTest {
}
