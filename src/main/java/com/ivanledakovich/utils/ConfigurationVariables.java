package com.ivanledakovich.utils;

import com.ivanledakovich.models.DatabaseConnectionProperties;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationVariables {

    private static final Logger logger = Logger.getLogger(ConfigurationVariables.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream configStream = getResourceAsStream("/config.properties")) {
            if (configStream != null) {
                logger.info("Loading config.properties...");
                properties.load(configStream);
            } else {
                logger.warn("config.properties not found. Trying defaultConfig.properties...");
                try (InputStream defaultStream = getResourceAsStream("/defaultConfig.properties")) {
                    if (defaultStream != null) {
                        logger.info("Loading defaultConfig.properties...");
                        properties.load(defaultStream);
                    } else {
                        throw new IllegalArgumentException("Neither config.properties nor defaultConfig.properties found in classpath");
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error loading configuration files", e);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static DatabaseConnectionProperties getDatabaseConnectionProperties() {
        DatabaseConnectionProperties databaseConnectionProperties = new DatabaseConnectionProperties();
        databaseConnectionProperties.setUrl(properties.getProperty("DB_URL"));
        databaseConnectionProperties.setUsername(properties.getProperty("DB_USERNAME"));
        databaseConnectionProperties.setPassword(properties.getProperty("DB_PASSWORD"));
        databaseConnectionProperties.setDriver(properties.getProperty("DB_DRIVER"));
        return databaseConnectionProperties;
    }

    public static Properties getConfigProperties() {
        return properties;
    }

    private static InputStream getResourceAsStream(String resourceName) {
        return ConfigurationVariables.class.getResourceAsStream(resourceName);
    }

    public static String getStorageType() {
        return properties.getProperty("STORAGE_TYPE", "database");
    }

    public static String getStoragePath() {
        return properties.getProperty("STORAGE_PATH", "./filestorage");
    }
}
