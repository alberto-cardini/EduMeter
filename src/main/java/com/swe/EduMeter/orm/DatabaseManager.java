package com.swe.EduMeter.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {}

    public static synchronized DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String dbUrl = System.getenv("DB_URL");
                String dbUser = System.getenv("DB_USER");
                String dbPassword = System.getenv("DB_PASSWORD");

                if (dbUrl == null || dbUser == null || dbPassword == null) {
                    throw new IllegalStateException("Environment variables (DB_URL, DB_USER, DB_PASSWORD) are missing.");
                }

                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            }
            return connection;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found.", e);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }
}