package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.database.FileSystemRepository;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ConfigurationVariables;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FileService {

    private final FileRepository repository;

    public FileService() {
        String storageType = ConfigurationVariables.getStorageType();

        if ("file_system".equalsIgnoreCase(storageType)) {
            this.repository = new FileSystemRepository(
                    ConfigurationVariables.getStoragePath()
            );
        } else {
            this.repository = new FileDatabaseFunctions(
                    ConfigurationVariables.getDatabaseConnectionProperties()
            );
        }
    }

    public void insertFile(File txtFile, File imageFile) throws IOException, SQLException {
        repository.insertAFile(txtFile, imageFile);
    }

    public List<FileModel> getAllFiles() throws SQLException, IOException, URISyntaxException {
        return repository.getAllFiles();
    }

    public FileModel getFile(String fileName) throws SQLException {
        return repository.getFileByName(fileName);
    }

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void processTxtFilesIntoImages(String imageExtension, String uploadPath, String convertedPath)
            throws IOException, SQLException, InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            File txtFilesDir = new File(uploadPath);
            File[] txtFileDirectoryListing = txtFilesDir.listFiles();
            if (txtFileDirectoryListing != null) {
                List<Future<?>> futures = new ArrayList<>();

                for (File txtFile : txtFileDirectoryListing) {
                    futures.add(executor.submit(
                            new FileProcessor(imageExtension, convertedPath, txtFile.getAbsolutePath())
                    ));
                }

                // Wait for all tasks to complete
                for (Future<?> future : futures) {
                    future.get();
                }
            }
        } finally {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}