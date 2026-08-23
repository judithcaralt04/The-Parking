package Persistance.dao;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Configuration loader that reads database and application settings.
 */
public class ConfigLoader {

    private static ConfigLoader instance = null;

    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPassword;
    private final String adminPassword;
    private final int simulatedVehicleDelay;

    /**
     * Creates a new ConfigLoader instance.
     *
     * @param path the path to the configuration file
     * @return the requested value
     * @throws Exception if the operation cannot be completed
     */
    private ConfigLoader(String path) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        String flat = content.replaceAll("\\s+", " ");

        this.dbHost = extractString(flat, "host");
        this.dbPort = Integer.parseInt(extractValue(flat, "port"));
        this.dbName = extractString(flat, "name");
        this.dbUser = extractString(flat, "user");
        this.dbPassword = extractString(flat, "password");
        this.adminPassword = extractString(flat, "adminPassword");
        this.simulatedVehicleDelay = Integer.parseInt(extractValue(flat, "simulatedVehicleDelay"));
    }

    /**
     * Returns instance.
     *
     * @return the shared configuration loader instance
     */
    public static ConfigLoader getInstance() {
        if (instance == null) {
            try {
                instance = new ConfigLoader("config.json");
            } catch (Exception e) {
                System.err.println("Error reading config.json: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    /**
     * Returns extract string.
     *
     * @param json the JSON text to inspect
     * @param key the key to extract from the JSON text
     * @return the requested text value
     */
    private String extractString(String json, String key) {
        String search = "\"" + key + "\"";
        int keyIndex = json.indexOf(search);

        if (keyIndex < 0) {
            throw new RuntimeException("Key not found in config.json: " + key);
        }

        int colon = json.indexOf(":", keyIndex);
        int open = json.indexOf("\"", colon + 1);
        int close = json.indexOf("\"", open + 1);

        return json.substring(open + 1, close);
    }

    /**
     * Returns extract value.
     *
     * @param json the JSON text to inspect
     * @param key the key to extract from the JSON text
     * @return the requested text value
     */
    private String extractValue(String json, String key) {
        String search = "\"" + key + "\"";
        int keyIndex = json.indexOf(search);

        if (keyIndex < 0){
            throw new RuntimeException("Key not found in config.json: " + key);
        }

        int colon = json.indexOf(":", keyIndex);
        int start = colon + 1;

        while (start < json.length() && json.charAt(start) == ' ') {
            start++;
        }
        int end = start;

        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) {
            end++;
        }
        return json.substring(start, end).trim();
    }

    /**
     * Returns db host.
     *
     * @return the requested text value
     */
    public String getDbHost() {
        return dbHost;
    }

    /**
     * Returns db port.
     *
     * @return the calculated numeric value
     */
    public int getDbPort() {
        return dbPort;
    }

    /**
     * Returns db name.
     *
     * @return the requested text value
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Returns db user.
     *
     * @return the requested text value
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Returns db password.
     *
     * @return the requested text value
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Returns admin password.
     *
     * @return the requested text value
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Returns simulated vehicle delay.
     *
     * @return the calculated numeric value
     */
    public int getSimulatedVehicleDelay() {
        return simulatedVehicleDelay;
    }
}
