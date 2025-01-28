package com.ivanledakovich.utils;

import com.ivanledakovich.models.DatabaseDao;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationVariables {

    private static final Logger logger = Logger.getLogger(ConfigurationVariables.class);

    public static DatabaseDao getEnvironmentVariables() {
        DatabaseDao databaseDao = new DatabaseDao();
        databaseDao.setUrl(System.getenv().get("DB_URL"));
        databaseDao.setUsername(System.getenv().get("DB_USERNAME"));
        databaseDao.setPassword(System.getenv().get("DB_PASSWORD"));
        databaseDao.setDriver(System.getenv().get("DB_DRIVER"));
        return databaseDao;
    }

    public static Properties getConfigProperties() {
        Properties properties = new Properties();
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
        return properties;
    }

    private static InputStream getResourceAsStream(String resourceName) {
        return ConfigurationVariables.class.getResourceAsStream(resourceName);
    }
}
