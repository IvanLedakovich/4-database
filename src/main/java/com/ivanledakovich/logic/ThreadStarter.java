package com.ivanledakovich.logic;

import com.ivanledakovich.utils.DatabaseFunctions;
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
                DatabaseFunctions databaseFunctions = new DatabaseFunctions();
                databaseFunctions.insertAFile(child);
                FileDeleteStrategy.FORCE.delete(child);
            }
        }
    }
}