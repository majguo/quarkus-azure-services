package io.quarkiverse.azure.cosmos.runtime;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;

import io.quarkiverse.azure.core.util.AzureQuarkusIdentifier;
import io.quarkus.runtime.configuration.ConfigurationException;

public class CosmosClientProducer {

    @Inject
    CosmosConfig cosmosConfiguration;

    @Produces
    public CosmosClient createCosmosClient() {
        CosmosClientBuilder builder = getBuilder();
        return null == builder ? null : builder.buildClient();
    }

    private CosmosClientBuilder getBuilder() {
        if (!cosmosConfiguration.enabled()) {
            return null;
        }

        String endpoint = cosmosConfiguration.endpoint()
                .orElseThrow(() -> new ConfigurationException(
                        "The endpoint of Azure Cosmos DB (quarkus.azure.cosmos.endpoint) must be set"));
        CosmosClientBuilder builder = new CosmosClientBuilder()
                .userAgentSuffix(AzureQuarkusIdentifier.AZURE_QUARKUS_COSMOS)
                .endpoint(endpoint);
        if (cosmosConfiguration.key().isPresent()) {
            builder.key(cosmosConfiguration.key().get());
        } else {
            builder.credential(new DefaultAzureCredentialBuilder().build());
        }
        if (cosmosConfiguration.defaultGatewayMode()) {
            builder.gatewayMode();
        }
        return builder;
    }
}
