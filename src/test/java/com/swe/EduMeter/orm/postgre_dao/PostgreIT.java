package com.swe.EduMeter.orm.postgre_dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class PostgreIT {

    static final PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:16")
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withInitScript("init.sql");
        postgres.start();

        DatabaseManager.initWithCredentials(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
    }

    @AfterEach
    void cleanUp() throws SQLException {
        try (var stmt = DatabaseManager.getInstance().getConnection().createStatement()) {
            stmt.execute("TRUNCATE Users, Admin, Pin, School, Professor, Degree, Course, Teaching," +
                             "Drafted_Review, Published_Review, Up_vote, Report RESTART IDENTITY CASCADE");
        }
    }
}
