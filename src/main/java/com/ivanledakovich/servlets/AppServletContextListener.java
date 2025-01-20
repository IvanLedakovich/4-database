package com.ivanledakovich.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class contains context logic
 *
 * @author Ivan Ledakovich
 */
public class AppServletContextListener implements ServletContextListener {
    private static Properties properties = new Properties();

    static {
        try {
            InputStream is = AppServletContextListener.class.getClassLoader().getResourceAsStream(".env");
            properties.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {}

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("myEnvironment", properties);
    }
}
