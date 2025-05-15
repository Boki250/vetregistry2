package tra4.bogdan.vetregistry2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/vet_registry_2";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "root";

    // Environment variable names
    private static final String ENV_DB_URL = "VET_REGISTRY_DB_URL";
    private static final String ENV_DB_USER = "VET_REGISTRY_DB_USER";
    private static final String ENV_DB_PASSWORD = "VET_REGISTRY_DB_PASSWORD";

    public static Connection connect() {
        Connection conn = null;
        try {
            // Get connection parameters from environment variables or use defaults
            String url = getEnvOrDefault(ENV_DB_URL, DEFAULT_URL);
            String user = getEnvOrDefault(ENV_DB_USER, DEFAULT_USER);
            String password = getEnvOrDefault(ENV_DB_PASSWORD, DEFAULT_PASSWORD);

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * Gets a value from an environment variable or returns the default value if not set
     * 
     * @param envName The name of the environment variable
     * @param defaultValue The default value to return if the environment variable is not set
     * @return The value from the environment variable or the default value
     */
    private static String getEnvOrDefault(String envName, String defaultValue) {
        String value = System.getenv(envName);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}
