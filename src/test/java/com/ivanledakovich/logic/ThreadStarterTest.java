package com.ivanledakovich.logic;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ThreadStarterTest {

    @Rule
    public TemporaryFolder testInputFolder = new TemporaryFolder();

    @Rule
    public TemporaryFolder testOutputFolder = new TemporaryFolder();

    private CountDownLatch lock = new CountDownLatch(1);


    @Test
    public void verifyTestOutputFileExists() throws Exception {
        // given
        File testInputFile = File.createTempFile("test", ".txt", new File(testInputFolder.getRoot().getPath()));
        FileUtils.write(testInputFile, "test", StandardCharsets.UTF_8);

        // when
        Main.main(new String[] {"--file-type", "png", "--file-path", testInputFile.getPath(), "--save-location", testOutputFolder.getRoot().getPath()});
        lock.await(2000, TimeUnit.MILLISECONDS);

        // then
        assertTrue(new File(testOutputFolder.getRoot().getPath() + "\\" + testInputFile.getName() + ".png").exists());
    }

    @Test
    public void verifyTestOutputResultIsCorrect() throws IOException, InterruptedException, URISyntaxException, SQLException {
        // given
        String fileName = "control-sample.png";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        assert resource != null;
        Path path = Path.of(resource.toURI());
        File testInputFile = File.createTempFile("test", ".txt", new File(testInputFolder.getRoot().getPath()));
        FileUtils.write(testInputFile, "test", StandardCharsets.UTF_8);

        // when
        Main.main(new String[] {"--file-type", "png", "--file-path", testInputFile.getPath(), "--save-location", testOutputFolder.getRoot().getPath()});
        lock.await(2000, TimeUnit.MILLISECONDS);
        Path outputFilePath = Path.of(testOutputFolder.getRoot().getPath() + "\\" + testInputFile.getName() + ".PNG");

        // then
        assertEquals(-1, Files.mismatch(outputFilePath, path));
    }

    @Test
    public void verifyArgumentsCasingHasNoEffect() throws IOException, InterruptedException, URISyntaxException, SQLException {
        // given
        String fileName = "control-sample.png";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        assert resource != null;
        Path path = Path.of(resource.toURI());
        File testInputFile = File.createTempFile("TEST", ".TXT", new File(testInputFolder.getRoot().getPath()));
        FileUtils.write(testInputFile, "test", StandardCharsets.UTF_8);

        // when
        Main.main(new String[] {"--FILE-TYPE", "PNG", "--FILE-PATH", testInputFile.getPath(), "--SAVE-LOCATION", testOutputFolder.getRoot().getPath()});
        lock.await(2000, TimeUnit.MILLISECONDS);
        Path outputFilePath = Path.of(testOutputFolder.getRoot().getPath() + "\\" + testInputFile.getName() + ".PNG");

        // then
        assertEquals(-1, Files.mismatch(outputFilePath, path));
    }
}
