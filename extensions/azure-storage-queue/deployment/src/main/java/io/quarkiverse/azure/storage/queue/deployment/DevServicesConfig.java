package io.quarkiverse.azure.storage.queue.deployment;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class DevServicesConfig {

    /**
     * If DevServices has been explicitly enabled or disabled. DevServices is generally enabled by
     * default, unless there is an existing configuration present.
     * <p>
     * When DevServices is enabled Quarkus will attempt to automatically configure and start an
     * azurite instance when running in Dev or Test mode and when Docker is running.
     */
    @ConfigItem(defaultValue = "true")
    public boolean enabled;

    /**
     * The container image name to use, for container based DevServices providers.
     */
    @ConfigItem(defaultValue = DevServicesStorageQueueProcessor.IMAGE)
    public Optional<String> imageName;

    /**
     * Optional fixed port the Dev services will listen to.
     * <p>
     * If not defined, the port will be chosen randomly.
     */
    @ConfigItem
    public OptionalInt port;

    /**
     * Indicates if the azurite instance managed by Quarkus Dev Services is shared. When shared,
     * Quarkus looks for running containers using label-based service discovery. If a matching
     * container is found, it is used, and so a second one is not started. Otherwise, Dev Services
     * for Azure Storage Queue starts a new container.
     * <p>
     * The discovery uses the {@code quarkus-dev-service-azure-storage-queue} label. The value is
     * configured using the {@code service-name} property.
     * <p>
     * Container sharing is only used in dev mode.
     */
    @ConfigItem(defaultValue = "true")
    public boolean shared;

    /**
     * The value of the {@code quarkus-dev-service-azure-storage-queue} label attached to the
     * started container. This property is used when {@code shared} is set to {@code true}. In this
     * case, before starting a container, Dev Services for Azure Storage Queue looks for a container
     * with the {@code quarkus-dev-service-azure-storage-queue} label set to the configured value.
     * If found, it will use this container instead of starting a new one. Otherwise it starts a new
     * container with the {@code quarkus-dev-service-azure-storage-queue} label set to the specified
     * value.
     * <p>
     * This property is used when you need multiple shared azurite instances.
     */
    @ConfigItem(defaultValue = "default-storage-queue")
    public String serviceName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DevServicesConfig that = (DevServicesConfig) o;
        return enabled == that.enabled && Objects.equals(imageName,
                that.imageName) && Objects.equals(port,
                        that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, imageName, port);
    }

    @Override
    public String toString() {
        return "DevServicesConfig{" +
                "enabled=" + enabled +
                ", imageName=" + imageName +
                ", port=" + port +
                ", shared=" + shared +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
