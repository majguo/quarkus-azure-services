package io.quarkiverse.azure.jdbc.postgresql.runtime;

import java.util.Map;

import io.agroal.api.configuration.supplier.AgroalDataSourceConfigurationSupplier;
import io.quarkus.agroal.runtime.AgroalConnectionConfigurer;
import io.quarkus.agroal.runtime.JdbcDriver;
import io.quarkus.datasource.common.runtime.DatabaseKind;

@JdbcDriver(DatabaseKind.POSTGRESQL)
public class AzurePostgreSQLAgroalConnectionConfigurer implements AgroalConnectionConfigurer {

    public void addAuthenticationPlugin(String databaseKind, AgroalDataSourceConfigurationSupplier dataSourceConfiguration,
                                  Map<String, String> additionalProperties) {
        dataSourceConfiguration.connectionPoolConfiguration().connectionFactoryConfiguration().jdbcProperty("sslmode",
                "disable");
    }

}
