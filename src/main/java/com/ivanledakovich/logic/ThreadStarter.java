package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.utils.ConfigurationVariables;
import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ThreadStarter {

    public void startThreads(String imageExtension, String uploadPath, String convertedPath) throws IOException, SQLException {
        File dir = new File(uploadPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                Thread thread = new Thread();
                thread.startANewThread(imageExtension, convertedPath, child.getAbsolutePath());
                FileDatabaseFunctions fileDatabaseFunctions = new FileDatabaseFunctions(ConfigurationVariables.getDatabaseConnectionProperties());
                fileDatabaseFunctions.insertAFile(child);
                FileDeleteStrategy.FORCE.delete(child);
            }
        }
    }
}