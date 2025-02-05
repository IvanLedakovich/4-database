package com.ivanledakovich.database;

import com.ivanledakovich.models.DatabaseConnectionProperties;
import com.ivanledakovich.models.FileModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class FileDatabaseFunctionsTest {

    private FileDatabaseFunctions fileDatabaseFunctions;

    @Before
    public void setUp() {
        DatabaseConnectionProperties h2Props = new DatabaseConnectionProperties(
                "org.h2.Driver",
                "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
                "sa",
                ""
        );
        fileDatabaseFunctions = new FileDatabaseFunctions(h2Props);
    }

    @After
    public void tearDown() throws SQLException {
        try (Connection connection = fileDatabaseFunctions.connect();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM files");
        }
    }

    private File createTextFile(String prefix, String content) throws IOException {
        File file = File.createTempFile(prefix, ".txt");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }

    private File createImageFile(String prefix) throws IOException {
        File file = File.createTempFile(prefix, ".png");
        file.deleteOnExit();
        return file;
    }

    @Test
    public void getFileByName_WhenFileExists_ReturnsFileModel() throws IOException, SQLException {
        File textFile = createTextFile("testFile", "Test content");
        File imageFile = createImageFile("testImage");

        fileDatabaseFunctions.insertAFile(textFile, imageFile);
        FileModel result = fileDatabaseFunctions.getFileByName(textFile.getName());

        assertNotNull("FileModel should not be null", result);
        assertEquals("File name should match", textFile.getName(), result.getFileName());
        assertNotNull("Image data should be present", result.getImageData());
    }

    @Test
    public void getFileByName_WhenFileDoesNotExist_ReturnsEmptyFileModel() throws SQLException {
        FileModel result = fileDatabaseFunctions.getFileByName("nonExistingFile");
        assertNull("File name should be null", result.getFileName());
    }

    @Test
    public void getAllFiles_AfterInsertions_ReturnsAllEntries() throws SQLException, IOException, URISyntaxException {
        File textFile1 = createTextFile("testFile1", "Content 1");
        File imageFile1 = createImageFile("testImage1");
        File textFile2 = createTextFile("testFile2", "Content 2");
        File imageFile2 = createImageFile("testImage2");

        fileDatabaseFunctions.insertAFile(textFile1, imageFile1);
        fileDatabaseFunctions.insertAFile(textFile2, imageFile2);

        List<FileModel> results = fileDatabaseFunctions.getAllFiles();

        assertEquals("Should return 2 entries", 2, results.size());
        assertTrue("Results should contain first file", results.stream().anyMatch(f -> textFile1.getName().equals(f.getFileName())));
        assertTrue("Results should contain second file", results.stream().anyMatch(f -> textFile2.getName().equals(f.getFileName())));
    }

    @Test
    public void insertAFile_WithValidFiles_PersistsInDatabase() throws IOException, SQLException {
        File textFile = createTextFile("testInsert", "Test content");
        File imageFile = createImageFile("testInsertImage");

        fileDatabaseFunctions.insertAFile(textFile, imageFile);
        FileModel result = fileDatabaseFunctions.getFileByName(textFile.getName());

        assertEquals("File name should match", textFile.getName(), result.getFileName());
        assertEquals("Image name should match", imageFile.getName(), result.getImageName());
    }

    @Test(expected = SQLException.class)
    public void insertAFile_WithDuplicateFileName_ThrowsSQLException() throws IOException, SQLException {
        // Create temporary directories to hold the files
        File tempDir1 = Files.createTempDirectory("test1").toFile();
        tempDir1.deleteOnExit();
        File tempDir2 = Files.createTempDirectory("test2").toFile();
        tempDir2.deleteOnExit();

        File textFile1 = new File(tempDir1, "duplicate.txt");
        try (FileWriter writer = new FileWriter(textFile1)) {
            writer.write("Content 1");
        }
        textFile1.deleteOnExit();

        File imageFile1 = createImageFile("testImage1");

        File textFile2 = new File(tempDir2, "duplicate.txt");
        try (FileWriter writer = new FileWriter(textFile2)) {
            writer.write("Content 2");
        }
        textFile2.deleteOnExit();
        File imageFile2 = createImageFile("testImage2");

        fileDatabaseFunctions.insertAFile(textFile1, imageFile1);
        fileDatabaseFunctions.insertAFile(textFile2, imageFile2);
    }

    @Test
    public void deleteFileByName_WhenFileExists_RemovesFromDatabase() throws IOException, SQLException, URISyntaxException {
        File textFile = createTextFile("testDelete", "Test content");
        File imageFile = createImageFile("testDeleteImage");

        fileDatabaseFunctions.insertAFile(textFile, imageFile);

        FileModel initial = fileDatabaseFunctions.getFileByName(textFile.getName());
        assertNotNull("File should exist before deletion", initial.getFileName());

        fileDatabaseFunctions.deleteFileByName(textFile.getName());

        FileModel afterDeletion = fileDatabaseFunctions.getFileByName(textFile.getName());
        assertNull("File name should be null after deletion", afterDeletion.getFileName());

        List<FileModel> allFiles = fileDatabaseFunctions.getAllFiles();
        assertTrue("Database should be empty after deletion", allFiles.isEmpty());
    }
}