package com.ivanledakovich.logic;

import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.ivanledakovich.utils.ImageNamingSystem.nameAnImageByNameAndExtension;

public class FileProcessorStarter {
    private final FileService fileService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public FileProcessorStarter(FileService fileService) {
        this.fileService = fileService;
    }

    public void startThreads(String imageExtension, String uploadPath, String convertedPath) throws IOException, SQLException, InterruptedException {
        File txtFilesDir = new File(uploadPath);
        File imageFilesDir = new File(convertedPath);
        File[] txtFileDirectoryListing = txtFilesDir.listFiles();
        File[] imageFileDirectoryListing = imageFilesDir.listFiles();
        if (txtFileDirectoryListing != null) {
            for (File txtFile : txtFileDirectoryListing) {
                Future<Integer> test = executor.submit(new FileProcessor(imageExtension, convertedPath, txtFile.getAbsolutePath()));

                while(!test.isDone()) {
                    System.out.println("Calculating...");
                    Thread.sleep(300);
                }

                for (File imageFile : Objects.requireNonNull(imageFilesDir.listFiles())) {
                    if (imageFile.getName().equals(nameAnImageByNameAndExtension(txtFile.getName(), imageExtension))) {
                        fileService.insertFile(txtFile, imageFile);
                        FileDeleteStrategy.FORCE.delete(txtFile);
                    }
                }

            }

        }
    }
}