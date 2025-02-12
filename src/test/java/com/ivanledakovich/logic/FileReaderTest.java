package com.ivanledakovich.logic;

import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class FileReaderTest {

    @Test(expected = RuntimeException.class)
    public void readFile_WithInvalidPath_ThrowsException() {
        FileReader.readFile("invalid/path.txt");
    }

    @Test
    public void readFile_WithValidFile_ReturnsContent() throws URISyntaxException {
        String path = Paths.get(
                getClass().getClassLoader().getResource("test.txt").toURI()
        ).toString();

        String content = FileReader.readFile(path);
        assertEquals("test", content.trim());
    }
}