package com.ivanledakovich.servlets;

import com.ivanledakovich.utils.ConfigurationVariables;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class contains context logic
 *
 * Loads configuration from config.properties, with fallback to defaultConfig.properties.
 */
public class AppServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties = ConfigurationVariables.getConfigProperties();

        // Store all properties
        servletContextEvent.getServletContext().setAttribute("configProperties", properties);

        // Extract only save locations
        Map<String, String> saveLocations = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("saveLocation_")) {
                saveLocations.put(key, properties.getProperty(key));
            }
        }

        // Store save locations separately for dropdown use
        servletContextEvent.getServletContext().setAttribute("myEnvironment", saveLocations);
    }
}
