package com.ivanledakovich.database;

import com.ivanledakovich.models.DatabaseConnectionProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class FileDatabaseFunctionsTest {

    private FileDatabaseFunctions fileDatabaseFunctions;
    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        DatabaseConnectionProperties h2Props = new DatabaseConnectionProperties();
        h2Props.setDriver("org.h2.Driver");
        h2Props.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        h2Props.setUsername("sa");
        h2Props.setPassword("");

        fileDatabaseFunctions = new FileDatabaseFunctions(h2Props);
        connection = DriverManager.getConnection(
                h2Props.getUrl(),
                h2Props.getUsername(),
                h2Props.getPassword()
        );
    }

    @After
    public void tearDown() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS");
        }
        connection.close();
    }

    private File createTextFile(String prefix, String content) throws IOException {
        File file = File.createTempFile(prefix, ".txt");
        file.deleteOnExit();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }

    private File createImageFile(String name) throws IOException {
        File file = File.createTempFile("test", name);
        file.deleteOnExit();
        return file;
    }

    @Test(expected = SQLException.class)
    public void insertAFile_WithDuplicateImageName_ThrowsSQLException() throws Exception {
        File textFile1 = createTextFile("test1", "Content1");
        File imageFile1 = createImageFile("duplicate.png");
        fileDatabaseFunctions.insertAFile(textFile1, imageFile1);
        connection.commit();

        File textFile2 = createTextFile("test2", "Content2");
        File imageFile2 = new File(imageFile1.getAbsolutePath());
        fileDatabaseFunctions.insertAFile(textFile2, imageFile2);
        connection.commit();
    }
}