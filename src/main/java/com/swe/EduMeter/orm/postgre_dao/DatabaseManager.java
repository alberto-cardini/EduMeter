package com.swe.EduMeter.orm.postgre_dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    // Default constructor which uses environment variables
    // to initiate the connection. For production only.
    private DatabaseManager() {
        dbUrl = System.getenv("DB_URL");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new IllegalArgumentException("Environment variables (DB_URL, DB_USER, DB_PASSWORD) are missing.");
        }
    }

    // Testing constructor, which accepts connection options.
    private DatabaseManager(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        return instance;
    }

    static void initWithCredentials(String dbUrl, String dbUser, String dbPassword) {
        instance = new DatabaseManager(dbUrl, dbUser, dbPassword);
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
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