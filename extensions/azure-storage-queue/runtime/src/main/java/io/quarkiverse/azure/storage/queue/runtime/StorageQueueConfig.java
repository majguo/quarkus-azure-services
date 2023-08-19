package io.quarkiverse.azure.storage.queue.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = "azure.storage.queue", phase = ConfigPhase.RUN_TIME)
public class StorageQueueConfig {

    /**
     * The connection string of Azure Storage Account.
     */
    @ConfigItem
    public String connectionString;
}
