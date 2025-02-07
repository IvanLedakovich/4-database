package com.ivanledakovich.logic;

import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ThreadStarter {
    private final FileService fileService;

    public ThreadStarter(FileService fileService) {
        this.fileService = fileService;
    }

    public void startThreads(String imageExtension, String uploadPath, String convertedPath) throws IOException, SQLException {
        File txtFilesDir = new File(uploadPath);
        File imageFilesDir = new File(convertedPath);
        File[] directoryListing = txtFilesDir.listFiles();
        if (directoryListing != null) {
            for (File txtFile : directoryListing) {
                Thread thread = new Thread();
                thread.startANewThread(imageExtension, convertedPath, txtFile.getAbsolutePath());
                for (File imageFile : Objects.requireNonNull(imageFilesDir.listFiles())) {
                    if (imageFile.getName().equals(txtFile.getName() + "." + imageExtension)) {
                        fileService.insertFile(txtFile, imageFile);
                        FileDeleteStrategy.FORCE.delete(txtFile);
                    }
                }
            }
        }
    }
}