package com.ivanledakovich.database;

import com.ivanledakovich.logic.FileWriter;
import com.ivanledakovich.models.DatabaseConnectionProperties;
import com.ivanledakovich.models.FileModel;

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
        String fileName = child.getName();
        try (FileInputStream fis = new FileInputStream(child);
             Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("INSERT INTO files(file_name, file_data) VALUES (?, ?)")) {
            prtmt.setString(1, fileName);
            prtmt.setBinaryStream(2, fis, (int) child.length());
            prtmt.executeUpdate();
        }
    }

    public FileModel getFileByName(String fileName) throws SQLException, IOException, URISyntaxException {
        try (Connection con = connect();
             PreparedStatement prtmt = con.prepareStatement("SELECT * FROM files WHERE file_name = ?")) {
            prtmt.setString(1, fileName);
            ResultSet rs = prtmt.executeQuery();
            FileModel file = new FileModel();

            while (rs.next()) {
                byte[] fileBytes = FileWriter.writeFile(rs);
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
                FileWriter.writeFile(rs);

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
