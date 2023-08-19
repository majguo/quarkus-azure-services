package io.quarkiverse.azure.storage.blob.deployment;

import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class StorageQueueTestResource implements QuarkusTestResourceLifecycleManager {

    static String accountName = "devstoreaccount1";
    static String accountKey = "Eby8vdM02xNOcqFlqUwJPLlmEtlCDXJ1OUzFT50uSRZ6IFsuFq2UVErCz4I6tq/K1SZFPTOtr/KBHBeksoGMGw==";
    static String image = "mcr.microsoft.com/azure-storage/azurite:3.25.0";
    static int port = 10001; // 10000 = storage blob, 10001 = storage queue, 10002 = storage table
    static String protocol = "http";

    static GenericContainer<?> server = new GenericContainer<>(DockerImageName.parse(image)).withExposedPorts(port);

    @Override
    public Map<String, String> start() {
        server.start();
        return Map.of("quarkus.azure.storage.queue.connection-string", getConnectionString());
    }

    @Override
    public void stop() {
        server.stop();
    }

    public static String getConnectionString() {

        final String host = server.getHost();
        final Integer mappedPort = server.getMappedPort(port);
        final String queueEndpoint = String.format("%s://%s:%s/%s", protocol, host, mappedPort, accountName);

        return String.format("DefaultEndpointsProtocol=%s;AccountName=%s;AccountKey=%s;QueueEndpoint=%s;",
                protocol, accountName, accountKey, queueEndpoint);
    }
}
