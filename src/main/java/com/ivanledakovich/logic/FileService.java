package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.database.FileRepository;
import com.ivanledakovich.database.FileSystemRepository;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ConfigurationVariables;
import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.ivanledakovich.utils.ImageNamingSystem.nameAnImageByNameAndExtension;

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

    public void processTxtFilesIntoImages(String imageExtension, String uploadPath, String convertedPath) throws IOException, SQLException, InterruptedException {
        File txtFilesDir = new File(uploadPath);
        File imageFilesDir = new File(convertedPath);
        File[] txtFileDirectoryListing = txtFilesDir.listFiles();
        if (txtFileDirectoryListing != null) {
            for (File txtFile : txtFileDirectoryListing) {
                Future<Integer> test = executor.submit(new FileProcessor(imageExtension, convertedPath, txtFile.getAbsolutePath()));

                while(!test.isDone()) {
                    Thread.sleep(300);
                }

                for (File imageFile : Objects.requireNonNull(imageFilesDir.listFiles())) {
                    if (imageFile.getName().equals(nameAnImageByNameAndExtension(txtFile.getName(), imageExtension))) {
                        insertFile(txtFile, imageFile);
                        FileDeleteStrategy.FORCE.delete(txtFile);
                    }
                }

            }

        }
    }
}