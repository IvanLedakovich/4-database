package com.ivanledakovich.database;

import com.ivanledakovich.models.DatabaseConnectionProperties;
import com.ivanledakovich.models.FileModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDatabaseFunctions {

    private DatabaseConnectionProperties databaseConnectionProperties;

    public FileDatabaseFunctions(DatabaseConnectionProperties databaseConnectionProperties) {
        this.databaseConnectionProperties = databaseConnectionProperties;
        createTableIfNotExists();
    }

    public Connection connect() {
        try {
            Class.forName(databaseConnectionProperties.getDriver());
            return DriverManager.getConnection(databaseConnectionProperties.getUrl(), databaseConnectionProperties.getUsername(), databaseConnectionProperties.getPassword());
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
                file_data BYTEA NOT NULL,
                image_name VARCHAR(255) NOT NULL,
                image_type VARCHAR(255) NOT NULL,
                image_data BYTEA NOT NULL
            )
        """;

        try (Connection con = connect();
             Statement stmt = con.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table 'files': " + e.getMessage(), e);
        }
    }

    public void insertAFile(File txtFile, File imageFile) throws SQLException, IOException {
        try (
                FileInputStream txtFis = new FileInputStream(txtFile);
                FileInputStream imageFis = new FileInputStream(imageFile);
             Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("INSERT INTO files(file_name, file_data, image_name, image_type, image_data) VALUES (?, ?, ?, ?, ?)")) {
            prtmt.setString(1, txtFile.getName());
            prtmt.setBinaryStream(2, txtFis, (int) txtFile.length());
            prtmt.setString(3, imageFile.getName());
            prtmt.setString(4, FilenameUtils.getExtension(imageFile.getName()));
            prtmt.setBinaryStream(5, imageFis, (int) imageFile.length());
            prtmt.executeUpdate();
        }
    }

    public FileModel getFileByName(String fileName) throws SQLException {
        try (Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("SELECT * FROM files WHERE file_name = ? OR image_name = ?")) {
            for (int i = 1; i <= 2; i++) {
                prtmt.setString(i, fileName);
            }
            ResultSet rs = prtmt.executeQuery();
            FileModel file = new FileModel();

            while (rs.next()) {
                file.setDate(rs.getDate("creation_date"));
                file.setFileName(rs.getString("file_name"));
                file.setFileData(rs.getBytes("file_data"));
                file.setImageName(rs.getString("image_name"));
                file.setImageType(rs.getString("image_type"));
                file.setImageData(rs.getBytes("image_data"));
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
                file.setId(rs.getInt("id"));
                file.setDate(rs.getDate("creation_date"));
                file.setFileName(rs.getString("file_name"));
                file.setImageName(rs.getString("image_name"));
                file.setImageType(rs.getString("image_type"));
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
