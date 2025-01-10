package io.quarkiverse.azure.services.together.it;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import io.smallrye.config.SmallRyeConfig;

@Path("/quarkus-services-app-configuration/config")
@Produces(MediaType.APPLICATION_JSON)
public class AppConfigurationConfigResource {
    @Inject
    SmallRyeConfig config;

    @GET
    @Path("/{name}")
    public String configValue(@PathParam("name") final String name) {
        return config.getConfigValue(name).getValue();
    }

}
