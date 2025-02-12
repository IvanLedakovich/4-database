package com.ivanledakovich.database;

import com.ivanledakovich.models.FileModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class FileSystemRepositoryTest {
    @Rule
    public TemporaryFolder storageFolder = new TemporaryFolder();

    private FileSystemRepository repository;
    private File storagePath;

    @Before
    public void setUp() throws IOException {
        storagePath = storageFolder.newFolder("filestorage");
        repository = new FileSystemRepository(storagePath.getAbsolutePath());
    }

    private File createTextFile() throws IOException {
        File file = new File(storagePath, "test.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Test content");
        }
        return file;
    }

    private File createImageFile() throws IOException {
        File file = new File(storagePath, "test.png");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("PNG content");
        }
        return file;
    }

    @Test
    public void insertAFile_PersistsDataCorrectly() throws Exception {
        File txtFile = createTextFile();
        File imgFile = createImageFile();

        repository.insertAFile(txtFile, imgFile);
        FileModel result = repository.getFileByName(txtFile.getName());

        assertNotNull(result);
        assertEquals(txtFile.getName(), result.getFileName());
        assertTrue(new File(storagePath, result.getImageName()).exists());
    }

    @Test
    public void deleteFile_RemovesFromStorage() throws Exception {
        File txtFile = createTextFile();
        File imgFile = createImageFile();

        repository.insertAFile(txtFile, imgFile);
        repository.deleteFileByName(txtFile.getName());

        assertFalse("Text file should be deleted", new File(storagePath, txtFile.getName()).exists());
        assertFalse("Image file should be deleted", new File(storagePath, imgFile.getName()).exists());

        List<FileModel> files = repository.getAllFiles();
        assertTrue("Metadata should be empty", files.isEmpty());
    }
}