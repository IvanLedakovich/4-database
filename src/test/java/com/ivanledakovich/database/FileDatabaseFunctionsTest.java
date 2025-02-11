//package com.ivanledakovich.database;
//
//import com.ivanledakovich.models.DatabaseConnectionProperties;
//import com.ivanledakovich.models.FileModel;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class FileDatabaseFunctionsTest {
//
//    private FileDatabaseFunctions fileDatabaseFunctions;
//
//    @Before
//    public void setUp() {
//        DatabaseConnectionProperties h2Props = new DatabaseConnectionProperties(
//                "org.h2.Driver",
//                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
//                "sa",
//                ""
//        );
//        fileDatabaseFunctions = new FileDatabaseFunctions(h2Props);
//    }
//
//    @After
//    public void tearDown() throws SQLException {
//        try (Connection connection = fileDatabaseFunctions.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.execute("DROP ALL OBJECTS");
//        }
//    }
//
//    private File createTextFile(String prefix, String content) throws IOException {
//        File file = File.createTempFile(prefix, ".txt");
//        file.deleteOnExit();
//        try (FileWriter writer = new FileWriter(file)) {
//            writer.write(content);
//        }
//        return file;
//    }
//
//    private File createImageFile(String prefix) throws IOException {
//        File file = File.createTempFile(prefix, ".png");
//        file.deleteOnExit();
//        return file;
//    }
//
//    @Test
//    public void getFileByName_WhenFileExists_ReturnsFileModel() throws Exception {
//        File textFile = createTextFile("testFile", "Test content");
//        File imageFile = createImageFile("testImage");
//
//        fileDatabaseFunctions.insertAFile(textFile, imageFile);
//        FileModel result = fileDatabaseFunctions.getFileByName(textFile.getName());
//
//        assertNotNull("FileModel should not be null", result);
//        assertEquals("File name should match", textFile.getName(), result.getFileName());
//        assertNotNull("Image data should be present", result.getImageData());
//    }
//
//    @Test
//    public void getFileByName_WhenFileDoesNotExist_ReturnsNull() throws SQLException {
//        FileModel result = fileDatabaseFunctions.getFileByName("nonExistingFile");
//        assertNull(result);
//    }
//
//    @Test
//    public void getAllFiles_AfterInsertions_ReturnsAllEntries() throws Exception {
//        File textFile1 = createTextFile("testFile1", "Content 1");
//        File imageFile1 = createImageFile("testImage1");
//        File textFile2 = createTextFile("testFile2", "Content 2");
//        File imageFile2 = createImageFile("testImage2");
//
//        fileDatabaseFunctions.insertAFile(textFile1, imageFile1);
//        fileDatabaseFunctions.insertAFile(textFile2, imageFile2);
//
//        List<FileModel> results = fileDatabaseFunctions.getAllFiles();
//
//        assertEquals("Should return 2 entries", 2, results.size());
//        assertTrue(results.stream().anyMatch(f -> textFile1.getName().equals(f.getFileName())));
//        assertTrue(results.stream().anyMatch(f -> textFile2.getName().equals(f.getFileName())));
//    }
//
//    @Test
//    public void insertAFile_WithValidFiles_PersistsInDatabase() throws Exception {
//        File textFile = createTextFile("testInsert", "Test content");
//        File imageFile = createImageFile("testInsertImage");
//
//        fileDatabaseFunctions.insertAFile(textFile, imageFile);
//        FileModel result = fileDatabaseFunctions.getFileByName(textFile.getName());
//
//        assertEquals(textFile.getName(), result.getFileName());
//        assertEquals(imageFile.getName(), result.getImageName());
//    }
//
//    @Test(expected = SQLException.class)
//    public void insertAFile_WithDuplicateFileName_ThrowsSQLException() throws Exception {
//        File textFile1 = createTextFile("duplicate", "Content 1");
//        File imageFile1 = createImageFile("testImage1");
//        File textFile2 = new File(textFile1.getAbsolutePath());
//        File imageFile2 = createImageFile("testImage2");
//
//        fileDatabaseFunctions.insertAFile(textFile1, imageFile1);
//        fileDatabaseFunctions.insertAFile(textFile2, imageFile2);
//    }
//
//    @Test
//    public void deleteFileByName_WhenFileExists_RemovesFromDatabase() throws Exception {
//        File textFile = createTextFile("testDelete", "Test content");
//        File imageFile = createImageFile("testDeleteImage");
//
//        fileDatabaseFunctions.insertAFile(textFile, imageFile);
//        fileDatabaseFunctions.deleteFileByName(textFile.getName());
//
//        FileModel result = fileDatabaseFunctions.getFileByName(textFile.getName());
//        assertNull(result);
//    }
//}