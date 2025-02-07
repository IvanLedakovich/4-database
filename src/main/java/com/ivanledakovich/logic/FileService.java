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
import java.util.List;

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

    public void deleteFile(String fileName) throws SQLException {
        repository.deleteFileByName(fileName);
    }
}