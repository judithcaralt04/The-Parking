package Persistance.dao;

import java.sql.*;

/**
 * Singleton helper that manages the database connection.
 */
public class SQLConnector {

    private static SQLConnector instance = null;

    private final String username;
    private final String password;
    private final String url;
    private Connection conn;
    private String lastError;

    /**
     * Creates a new SQLConnector instance.
     *
     * @return the requested value
     */
    private SQLConnector() {
        ConfigLoader cfg = ConfigLoader.getInstance();
        this.username = cfg.getDbUser();
        this.password = cfg.getDbPassword();
        this.url = "jdbc:mysql://" + cfg.getDbHost() + ":" + cfg.getDbPort() + "/" + cfg.getDbName();
    }

    /**
     * Returns instance.
     *
     * @return the shared SQL connector instance
     */
    public static SQLConnector getInstance() {
        if (instance == null) {
            instance = new SQLConnector();
            instance.connect();
        }
        return instance;
    }

    /**
     * Opens connect.
     */
    public void connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database: " + url);
        } catch (SQLException e) {
            conn = null;
            lastError = e.getMessage();
        }
    }

    /**
     * Closes disconnect.
     */
    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Returns connection.
     *
     * @return the active database connection
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Checks whether connected.
     *
     * @return true when the condition is met, false otherwise
     */
    public boolean isConnected() {
        return conn != null;
    }

    /**
     * Returns last error.
     *
     * @return the requested text value
     */
    public String getLastError() {
        return lastError;
    }

    /**
     * Returns url.
     *
     * @return the requested text value
     */
    public String getUrl() {
        return url;
    }
}
