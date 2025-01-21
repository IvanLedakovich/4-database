package com.ivanledakovich.servlets;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class contains context logic
 *
 * Loads configuration from config.txt, with fallback to defaultConfig.txt.
 */
public class AppServletContextListener implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(AppServletContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Properties properties = new Properties();
        try (InputStream configStream = getResourceAsStream("config.txt")) {
            if (configStream != null) {
                properties.load(configStream);
            } else {
                try (InputStream defaultStream = getResourceAsStream("defaultConfig.txt")) {
                    if (defaultStream != null) {
                        properties.load(defaultStream);
                    } else {
                        throw new IllegalArgumentException("Neither config.txt nor defaultConfig.txt found");
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error loading configuration files", e);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
        }
        servletContextEvent.getServletContext().setAttribute("myEnvironment", properties);
    }

    private InputStream getResourceAsStream(String resourceName) {
        return AppServletContextListener.class.getClassLoader().getResourceAsStream(resourceName);
    }
}
