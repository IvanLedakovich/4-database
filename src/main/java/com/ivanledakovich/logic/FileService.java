package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ConfigurationVariables;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

public class FileService {
    private FileDatabaseFunctions fileDbFunctions;

    public FileService() {
        this.fileDbFunctions = new FileDatabaseFunctions(ConfigurationVariables.getDatabaseConnectionProperties());
    }

    public List<FileModel> getAllFiles() throws SQLException, IOException, URISyntaxException {
        return fileDbFunctions.getAllFiles();
    }

    public FileModel getFile(String fileName) throws SQLException {
        return fileDbFunctions.getFileByName(fileName);
    }
}