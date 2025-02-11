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

public class FileDatabaseFunctions implements FileRepository {
    private final DatabaseConnectionProperties dbProps;

    public FileDatabaseFunctions(DatabaseConnectionProperties dbProps) {
        this.dbProps = dbProps;
        createTableIfNotExists();
        addMissingColumnsIfNotPresent();
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName(dbProps.getDriver());
            return DriverManager.getConnection(
                    dbProps.getUrl(),
                    dbProps.getUsername(),
                    dbProps.getPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found", e);
        }
    }

    private void createTableIfNotExists() {
        String sql = """
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

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Table creation failed", e);
        }
    }

    private void addMissingColumnsIfNotPresent() {
        String[] columnsToAdd = {
                "ALTER TABLE files ADD COLUMN IF NOT EXISTS image_name VARCHAR(255) UNIQUE NOT NULL",
                "ALTER TABLE files ADD COLUMN IF NOT EXISTS image_type VARCHAR(255) NOT NULL",
                "ALTER TABLE files ADD COLUMN IF NOT EXISTS image_data BYTEA NOT NULL"
        };

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            for (String sql : columnsToAdd) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Column addition failed", e);
        }
    }

    @Override
    public void insertAFile(File txtFile, File imageFile) throws IOException, SQLException {
        String sql = """
            INSERT INTO files(file_name, file_data, image_name, image_type, image_data) 
            VALUES (?, ?, ?, ?, ?)
        """;

        try (FileInputStream txtStream = new FileInputStream(txtFile);
             FileInputStream imgStream = new FileInputStream(imageFile);
             Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, txtFile.getName());
            pstmt.setBinaryStream(2, txtStream, (int) txtFile.length());
            pstmt.setString(3, imageFile.getName());
            pstmt.setString(4, FilenameUtils.getExtension(imageFile.getName()));
            pstmt.setBinaryStream(5, imgStream, (int) imageFile.length());

            pstmt.executeUpdate();
        }
    }

    @Override
    public FileModel getFileByName(String fileName) throws SQLException {
        String sql = "SELECT * FROM files WHERE file_name = ? OR image_name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileName);
            pstmt.setString(2, fileName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFileModel(rs);
                }
                return null;
            }
        }
    }

    @Override
    public List<FileModel> getAllFiles() throws SQLException, URISyntaxException, IOException {
        String sql = "SELECT * FROM files";
        List<FileModel> files = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                files.add(mapResultSetToFileModel(rs));
            }
        }
        return files;
    }

    @Override
    public void deleteFileByName(String fileName) throws SQLException {
        String sql = "DELETE FROM files WHERE file_name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fileName);
            pstmt.executeUpdate();
        }
    }

    private FileModel mapResultSetToFileModel(ResultSet rs) throws SQLException {
        FileModel model = new FileModel();
        model.setId(rs.getInt("id"));
        model.setDate(rs.getDate("creation_date"));
        model.setFileName(rs.getString("file_name"));
        model.setFileData(rs.getBytes("file_data"));
        model.setImageName(rs.getString("image_name"));
        model.setImageType(rs.getString("image_type"));
        model.setImageData(rs.getBytes("image_data"));
        return model;
    }

    Connection getConnection() throws SQLException {
        return connect();
    }
}