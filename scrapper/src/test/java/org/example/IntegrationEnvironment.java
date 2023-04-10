package org.example;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
public abstract class IntegrationEnvironment {
    @Container
    public static final JdbcDatabaseContainer<?> DATABASE_CONTAINER;
    static {
        DATABASE_CONTAINER = new PostgreSQLContainer<>("postgres:15-alpine")
                .withDatabaseName("scrapper")
                .withUsername("postgres")
                .withPassword("postgres");
        DATABASE_CONTAINER.start();
        Startables.deepStart(DATABASE_CONTAINER)
                .thenAccept(unused -> runMigrations());
    }

    private static void runMigrations() {
        var changelogPath = new File(".").toPath().toAbsolutePath().
                getParent().getParent().resolve("db/migrations");
        try(var conn = DriverManager
                .getConnection(IntegrationEnvironment.DATABASE_CONTAINER.getJdbcUrl(),
                        IntegrationEnvironment.DATABASE_CONTAINER.getUsername(),
                        IntegrationEnvironment.DATABASE_CONTAINER.getPassword())){
            var database = new PostgresDatabase();
            database.setConnection(new JdbcConnection(conn));
            var liquibase = new Liquibase("master.xml",
                    new DirectoryResourceAccessor(changelogPath), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (SQLException | FileNotFoundException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", DATABASE_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DATABASE_CONTAINER::getPassword);
    }
}
