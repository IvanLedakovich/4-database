package com.ivanledakovich.utils;

import com.ivanledakovich.models.FileModel;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class DatabaseFunctionsTest {

    private DatabaseFunctions databaseFunctions = new DatabaseFunctions();
    private static final Logger logger = Logger.getLogger(DatabaseFunctionsTest.class);

    private File getTestFile() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test.txt");
        if (resource == null) {
            throw new IllegalArgumentException("test.txt not found in resources folder");
        }
        return new File(resource.toURI());
    }

    private File createTemporaryFile(String fileName, String content) throws IOException {
        File tempFile = File.createTempFile(fileName, null);
        tempFile.deleteOnExit(); // Ensure the file is deleted when the JVM exits
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

    @Test
    public void testGetFileByName() throws URISyntaxException, SQLException, IOException {
        File testFile = getTestFile();

        try {
            databaseFunctions.insertAFile(testFile);
        } catch (SQLException | IOException e) {
            logger.error(e);
        }

        FileModel file = databaseFunctions.getFileByName(testFile.getName());
        Assert.assertEquals(testFile.getName(), file.getFile_name());
    }

    @Test
    public void testGetAllFiles() throws Exception {
        // Create two temporary test files
        File testFile1 = createTemporaryFile("testFile1.txt", "Content of test file 1");
        File testFile2 = createTemporaryFile("testFile2.txt", "Content of test file 2");

        try {
            // Insert the files into the database
            databaseFunctions.insertAFile(testFile1);
            databaseFunctions.insertAFile(testFile2);

            // Fetch all files from the database
            List<FileModel> files = databaseFunctions.getAllFiles();

            // Validate that both files are present in the retrieved list
            Assert.assertTrue(files.stream().anyMatch(f -> f.getFile_name().equals(testFile1.getName())));
            Assert.assertTrue(files.stream().anyMatch(f -> f.getFile_name().equals(testFile2.getName())));
        } finally {
            // Clean up: Remove the inserted files from the database
            databaseFunctions.deleteFileByName(testFile1.getName());
            databaseFunctions.deleteFileByName(testFile2.getName());

            // Delete temporary files
            testFile1.delete();
            testFile2.delete();
        }
    }

    @Test
    public void testInsertFile() throws Exception {
        // Create a temporary test file
        File testFile = createTemporaryFile("testInsertFile.txt", "This is a test file for insertion.");

        try {
            // Insert the file into the database
            databaseFunctions.insertAFile(testFile);

            // Fetch the file by name to validate it was inserted
            FileModel fetchedFile = databaseFunctions.getFileByName(testFile.getName());

            // Validate that the fetched file matches the inserted file
            Assert.assertNotNull("File should be present in the database", fetchedFile);
            Assert.assertEquals("File name should match", testFile.getName(), fetchedFile.getFile_name());
        } finally {
            // Clean up: Remove the inserted file from the database
            databaseFunctions.deleteFileByName(testFile.getName());

            // Delete the temporary file
            testFile.delete();
        }
    }
}
