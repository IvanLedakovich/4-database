package com.ivanledakovich.logic;

import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FileReaderTest {

    @Test
    public void verifyExceptionIsThrown() {
        assertThrows(RuntimeException.class, () -> {
            FileReader.readFile("");
        });
    }

    @Test
    public void verifyTheFilesAreReadCorrectly() throws URISyntaxException {
    // given
    String data = "";
    String fileName = "test.txt";
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    assert resource != null;
    String path = resource.getPath();

    // when
    data = FileReader.readFile(path);

    // then
    assertEquals(data, "test .txt file");
    }
}
