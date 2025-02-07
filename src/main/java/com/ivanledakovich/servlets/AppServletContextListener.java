package com.ivanledakovich.servlets;

import com.ivanledakovich.utils.ConfigurationVariables;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.*;

/**
 * This class contains context logic
 *
 * Loads configuration from config.properties, with fallback to defaultConfig.properties.
 */
public class AppServletContextListener implements ServletContextListener {
    private static final long CLEANUP_INTERVAL = 6 * 60 * 60 * 1000;
    private Timer timer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties properties = ConfigurationVariables.getConfigProperties();

        sce.getServletContext().setAttribute("configProperties", properties);

        Map<String, String> saveLocations = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("saveLocation_")) {
                saveLocations.put(key, properties.getProperty(key));
            }
        }

        sce.getServletContext().setAttribute("myEnvironment", saveLocations);

        timer = new Timer(true);
        timer.scheduleAtFixedRate(new CleanupTask(), 0, CLEANUP_INTERVAL);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (timer != null) {
            timer.cancel();
        }
    }

    private static class CleanupTask extends TimerTask {
        private static final long MAX_FILE_AGE = 24 * 60 * 60 * 1000;;

        public void run() {
            cleanTempDirectory();
        }

        private void cleanTempDirectory() {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File[] files = tempDir.listFiles();
            long now = System.currentTimeMillis();

            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith("uploadedFiles")) {
                        if (now - file.lastModified() > MAX_FILE_AGE) {
                            file.delete();
                        }
                    }
                }
            }
        }
    }
}
