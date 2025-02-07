package com.ivanledakovich.logic;

import com.ivanledakovich.utils.ConfigurationVariables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class ThreadStarterTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private FileService fileService;
    private Path uploadPath;
    private Path convertedPath;

    @Before
    public void setUp() throws Exception {
        ConfigurationVariables.getConfigProperties().setProperty("STORAGE_TYPE", "file_system");
        File storageFolder = tempFolder.newFolder("storage");
        ConfigurationVariables.getConfigProperties().setProperty("STORAGE_PATH", storageFolder.getAbsolutePath());

        uploadPath = tempFolder.newFolder("uploads").toPath();
        convertedPath = storageFolder.toPath();
        fileService = new FileService();
    }

    private File createTextFile() throws Exception {
        File file = uploadPath.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Test content");
        }
        return file;
    }

    @Test
    public void verifyTestOutputFileExists() throws Exception {
        File txtFile = createTextFile();
        ThreadStarter starter = new ThreadStarter(fileService);
        String saveLocation = ConfigurationVariables.getStoragePath();

        starter.startThreads("png", uploadPath.toString(), saveLocation);

        TimeUnit.SECONDS.sleep(3);

        File expectedImage = new File(saveLocation, txtFile.getName() + ".png");
        assertTrue("Output image should exist", expectedImage.exists());
    }

    @Test
    public void verifyArgumentsCasingHasNoEffect() throws Exception {
        File txtFile = createTextFile();
        ThreadStarter starter = new ThreadStarter(fileService);
        String saveLocation = ConfigurationVariables.getStoragePath();

        starter.startThreads("PNG", uploadPath.toString(), saveLocation);

        TimeUnit.SECONDS.sleep(3);

        File expectedImage = new File(saveLocation, txtFile.getName() + ".png");
        assertTrue("Case variations should not affect output", expectedImage.exists());
    }

    @Test
    public void verifyTestOutputResultIsCorrect() throws Exception {
        File txtFile = createTextFile();
        ThreadStarter starter = new ThreadStarter(fileService);
        String saveLocation = ConfigurationVariables.getStoragePath();

        starter.startThreads("png", uploadPath.toString(), saveLocation);

        TimeUnit.SECONDS.sleep(3);

        File storedFile = new File(saveLocation, txtFile.getName() + ".png");
        assertTrue("File should be in storage", storedFile.exists());
    }
}