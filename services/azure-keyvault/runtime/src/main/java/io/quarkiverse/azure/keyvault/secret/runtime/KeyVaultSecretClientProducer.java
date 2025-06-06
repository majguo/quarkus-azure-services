package io.quarkiverse.azure.keyvault.secret.runtime;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

import com.azure.core.util.ClientOptions;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretAsyncClient;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;

import io.quarkiverse.azure.core.util.AzureQuarkusIdentifier;
import io.quarkiverse.azure.keyvault.secret.runtime.config.KeyVaultSecretConfig;
import io.quarkus.runtime.configuration.ConfigurationException;

public class KeyVaultSecretClientProducer {

    @Inject
    KeyVaultSecretConfig secretConfiguration;

    @Produces
    public SecretClient createSecretClient() {
        if (!secretConfiguration.enabled()) {
            return null;
        }

        String endpoint = secretConfiguration.endpoint()
                .orElseThrow(() -> new ConfigurationException(
                        "The endpoint of Azure Key Vault Secret (quarkus.azure.keyvault.secret.endpoint) must be set"));
        return new SecretClientBuilder()
                .clientOptions(new ClientOptions().setApplicationId(AzureQuarkusIdentifier.AZURE_QUARKUS_KEY_VAULT_SYNC_CLIENT))
                .vaultUrl(endpoint)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    @Produces
    public SecretAsyncClient createSecretAsyncClient() {
        if (!secretConfiguration.enabled()) {
            return null;
        }

        String endpoint = secretConfiguration.endpoint()
                .orElseThrow(() -> new ConfigurationException("The endpoint of Azure Key Vault Secret must be set"));
        return new SecretClientBuilder()
                .clientOptions(
                        new ClientOptions().setApplicationId(AzureQuarkusIdentifier.AZURE_QUARKUS_KEY_VAULT_ASYNC_CLIENT))
                .vaultUrl(endpoint)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildAsyncClient();
    }
}
