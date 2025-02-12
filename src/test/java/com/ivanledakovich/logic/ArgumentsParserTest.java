package com.ivanledakovich.logic;

import com.ivanledakovich.models.Parameters;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ArgumentsParserTest {

    @Test
    public void parseArguments_WithValidArgs_ReturnsCorrectParameters() {
        String[] args = {
                "--file-type", "png",
                "--save-location", "/save/path",
                "--file-path", "file1.txt", "file2.txt"
        };

        Parameters params = ArgumentsParser.parseArguments(args);

        assertEquals("png", params.getImageFileType());
        assertEquals("/save/path", params.getImageSaveLocation());
        assertEquals(Arrays.asList("file1.txt", "file2.txt"), params.getAllTextFilePaths());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseArguments_WithMissingArgs_ThrowsException() {
        String[] args = {"--file-type", "png"};
        ArgumentsParser.parseArguments(args);
    }
}