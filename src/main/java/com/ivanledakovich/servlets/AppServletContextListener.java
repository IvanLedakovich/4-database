package com.ivanledakovich.servlets;

import com.ivanledakovich.utils.ConfigurationVariables;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
        servletContextEvent.getServletContext().setAttribute("myEnvironment", properties);
    }
}
