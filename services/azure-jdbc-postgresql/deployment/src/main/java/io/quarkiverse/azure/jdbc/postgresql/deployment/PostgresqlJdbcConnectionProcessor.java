package io.quarkiverse.azure.jdbc.postgresql.deployment;

import io.quarkiverse.azure.jdbc.postgresql.runtime.PostgresqlJdbcConnectionCustomizer;
import io.quarkus.agroal.spi.JdbcDataSourceBuildItem;
import io.quarkus.datasource.common.runtime.DatabaseKind;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;

import java.util.List;

/**
 * Processor for Azure PostgreSQL JDBC integration.
 * Enhances PostgreSQL connections to support Azure managed identity authentication.
 */
public class PostgresqlJdbcConnectionProcessor {

    private static final String FEATURE = "azure-jdbc-postgresql";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    void configureDataSources(PostgresqlJdbcConnectionCustomizer recorder,
                              List<JdbcDataSourceBuildItem> dataSources) {
        for (JdbcDataSourceBuildItem dataSource : dataSources) {
            if (DatabaseKind.isPostgreSQL(dataSource.getDbKind())) {
                recorder.configureDataSource(dataSource.getName());
            }
        }
    }
}
