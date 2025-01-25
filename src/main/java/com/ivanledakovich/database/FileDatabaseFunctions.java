package com.ivanledakovich.database;

import com.ivanledakovich.models.DatabaseDao;
import com.ivanledakovich.models.FileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDatabaseFunctions {

    private DatabaseDao databaseDao;

    public FileDatabaseFunctions(DatabaseDao databaseDao) {
        this.databaseDao = databaseDao;
        createTableIfNotExists();
    }

    public Connection connect() {
        try {
            Class.forName(databaseDao.getDriver());
            return DriverManager.getConnection(databaseDao.getUrl(), databaseDao.getUsername(), databaseDao.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createTableIfNotExists() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS files (
                id SERIAL PRIMARY KEY,
                creation_date DATE DEFAULT CURRENT_DATE,
                file_name VARCHAR(255) NOT NULL,
                file_data BYTEA NOT NULL
            )
        """;

        try (Connection con = connect();
             Statement stmt = con.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table 'files': " + e.getMessage(), e);
        }
    }

    public void insertAFile(File child) throws SQLException, IOException {
        String file_name = child.getName();
        try (FileInputStream fis = new FileInputStream(child);
             Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("INSERT INTO files(file_name, file_data) VALUES (?, ?)")) {
            prtmt.setString(1, file_name);
            prtmt.setBinaryStream(2, fis, (int) child.length());
            prtmt.executeUpdate();
        }
    }

    public FileModel getFileByName(String file_name) throws SQLException, IOException, URISyntaxException {
        try (Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("SELECT * FROM files WHERE file_name = ?")) {
            prtmt.setString(1, file_name);
            ResultSet rs = prtmt.executeQuery();
            FileModel file = new FileModel();

            while (rs.next()) {
                URL res = FileDatabaseFunctions.class.getClassLoader().getResource("uploadedFiles");
                File newFile = Paths.get(res.toURI()).toFile();
                String absolutePath = newFile.getAbsolutePath();
                File fileUploadDirectory = new File(absolutePath);
                if (!fileUploadDirectory.exists()) {
                    fileUploadDirectory.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(absolutePath + "//" + rs.getString("file_name") + ".txt");
                byte[] fileBytes = rs.getBytes("file_data");
                fos.write(fileBytes);
                fos.close();

                file.setDate(rs.getDate("creation_date"));
                file.setFileName(rs.getString("file_name"));
                file.setFileData(fileBytes);
            }

            return file;
        }
    }

    public List<FileModel> getAllFiles() throws SQLException, IOException, URISyntaxException {
        try (Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("SELECT * FROM files");
             ResultSet rs = prtmt.executeQuery()) {

            List<FileModel> files = new ArrayList<>();

            while (rs.next()) {
                FileModel file = new FileModel();
                URL res = FileDatabaseFunctions.class.getClassLoader().getResource("uploadedFiles");
                File newFile = Paths.get(res.toURI()).toFile();
                String absolutePath = newFile.getAbsolutePath();
                File fileUploadDirectory = new File(absolutePath);
                if (!fileUploadDirectory.exists()) {
                    fileUploadDirectory.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(absolutePath + "//" + rs.getString("file_name") + ".txt");
                byte[] fileBytes = rs.getBytes("file_data");
                fos.write(fileBytes);
                fos.close();

                file.setDate(rs.getDate("creation_date"));
                file.setFileName(rs.getString("file_name"));
                files.add(file);
            }

            return files;
        }
    }

    public void deleteFileByName(String fileName) throws SQLException {
        String query = "DELETE FROM files WHERE file_name = ?";
        try (Connection con = connect();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, fileName);
            stmt.executeUpdate();
        }
    }

}
