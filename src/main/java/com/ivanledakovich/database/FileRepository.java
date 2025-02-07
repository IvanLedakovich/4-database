package com.ivanledakovich.database;

import com.ivanledakovich.models.FileModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

/**
 * Contract for file storage operations.
 * @author Ivan Ledakovich
 */
public interface FileRepository {
    void insertAFile(File txtFile, File imageFile) throws IOException, SQLException;
    FileModel getFileByName(String fileName) throws SQLException;
    List<FileModel> getAllFiles() throws SQLException, IOException, URISyntaxException;
    void deleteFileByName(String fileName) throws SQLException;
}