package io.quarkiverse.azure.storage.queue.deployment;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "azure.storage.queue")
public class StorageQueueBuildTimeConfig {

    /**
     * Whether a health check is published in case the smallrye-health extension is present.
     */
    @ConfigItem(name = "health.enabled", defaultValue = "true")
    public boolean healthEnabled;

    /**
     * Dev Services configuration.
     */
    @ConfigItem
    public DevServicesConfig devservices;

    @Override
    public String toString() {
        return "StorageQueueBuildTimeConfig{" +
                "healthEnabled=" + healthEnabled +
                ", devservices=" + devservices +
                '}';
    }
}
