package com.ivanledakovich.database;

public class FileDatabaseFunctionsTest {

//    private FileDatabaseFunctions fileDatabaseFunctions;
//
//    @Before
//    public void setUp() {
//        DatabaseConnectionProperties h2Props = new DatabaseConnectionProperties(
//                "org.h2.Driver",
//                "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
//                "sa",
//                ""
//        );
//        fileDatabaseFunctions = new FileDatabaseFunctions(h2Props);
//    }
//
//    @After
//    public void tearDown() throws SQLException {
//        try (Connection connection = fileDatabaseFunctions.connect();
//             Statement statement = connection.createStatement()) {
//            statement.execute("DELETE FROM files");
//        }
//    }
//
//    private File getTestFile() throws URISyntaxException {
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource("test.txt");
//        if (resource == null) {
//            throw new IllegalArgumentException("test.txt not found in resources folder");
//        }
//        return new File(resource.toURI());
//    }
//
//    private File createTemporaryFile(String fileName, String content) throws IOException {
//        File tempFile = File.createTempFile(fileName, null);
//        tempFile.deleteOnExit();
//        try (FileWriter writer = new FileWriter(tempFile)) {
//            writer.write(content);
//        }
//        return tempFile;
//    }
//
//    @Test
//    public void testGetFileByName() throws URISyntaxException, SQLException, IOException {
//        File testFile = getTestFile();
//
//        fileDatabaseFunctions.insertAFile(testFile);
//        FileModel file = fileDatabaseFunctions.getFileByName(testFile.getName());
//
//        assertEquals(testFile.getName(), file.getFileName());
//    }
//
//    @Test
//    public void testGetAllFiles() throws Exception {
//        File testFile1 = createTemporaryFile("testFile1", "Content 1");
//        File testFile2 = createTemporaryFile("testFile2", "Content 2");
//
//        fileDatabaseFunctions.insertAFile(testFile1);
//        fileDatabaseFunctions.insertAFile(testFile2);
//
//        List<FileModel> files = fileDatabaseFunctions.getAllFiles();
//
//        assertTrue(files.stream().anyMatch(f -> f.getFileName().equals(testFile1.getName())));
//        assertTrue(files.stream().anyMatch(f -> f.getFileName().equals(testFile2.getName())));
//
//        testFile1.delete();
//        testFile2.delete();
//    }
//
//    @Test
//    public void testInsertFile() throws Exception {
//        File testFile = createTemporaryFile("testInsert", "Test content");
//
//        fileDatabaseFunctions.insertAFile(testFile);
//        FileModel fetchedFile = fileDatabaseFunctions.getFileByName(testFile.getName());
//
//        assertNotNull("File should be present in the database", fetchedFile);
//        assertEquals("File name should match", testFile.getName(), fetchedFile.getFileName());
//
//        testFile.delete();
//    }
}