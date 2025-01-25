package com.ivanledakovich.database;

import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ConfigurationVariables;
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

public class FileDatabaseFunctionsTest {

    private FileDatabaseFunctions fileDatabaseFunctions = new FileDatabaseFunctions(ConfigurationVariables.getEnvironmentVariables());
    private static final Logger logger = Logger.getLogger(FileDatabaseFunctionsTest.class);

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
        tempFile.deleteOnExit();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

    @Test
    public void testGetFileByName() throws URISyntaxException, SQLException, IOException {
        File testFile = getTestFile();

        try {
            fileDatabaseFunctions.insertAFile(testFile);
        } catch (SQLException | IOException e) {
            logger.error(e);
        }

        FileModel file = fileDatabaseFunctions.getFileByName(testFile.getName());
        Assert.assertEquals(testFile.getName(), file.getFileName());
    }

    @Test
    public void testGetAllFiles() throws Exception {
        File testFile1 = createTemporaryFile("testFile1.txt", "Content of test file 1");
        File testFile2 = createTemporaryFile("testFile2.txt", "Content of test file 2");

        try {
            fileDatabaseFunctions.insertAFile(testFile1);
            fileDatabaseFunctions.insertAFile(testFile2);

            List<FileModel> files = fileDatabaseFunctions.getAllFiles();

            Assert.assertTrue(files.stream().anyMatch(f -> f.getFileName().equals(testFile1.getName())));
            Assert.assertTrue(files.stream().anyMatch(f -> f.getFileName().equals(testFile2.getName())));
        } finally {
            fileDatabaseFunctions.deleteFileByName(testFile1.getName());
            fileDatabaseFunctions.deleteFileByName(testFile2.getName());

            testFile1.delete();
            testFile2.delete();
        }
    }

    @Test
    public void testInsertFile() throws Exception {
        File testFile = createTemporaryFile("testInsertFile.txt", "This is a test file for insertion.");

        try {
            fileDatabaseFunctions.insertAFile(testFile);

            FileModel fetchedFile = fileDatabaseFunctions.getFileByName(testFile.getName());

            Assert.assertNotNull("File should be present in the database", fetchedFile);
            Assert.assertEquals("File name should match", testFile.getName(), fetchedFile.getFileName());
        } finally {
            fileDatabaseFunctions.deleteFileByName(testFile.getName());

            testFile.delete();
        }
    }
}
