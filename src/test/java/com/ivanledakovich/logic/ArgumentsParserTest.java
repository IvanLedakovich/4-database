package com.ivanledakovich.logic;

import com.ivanledakovich.models.Parameters;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ArgumentsParserTest {

    @Test
    public void verifyArgumentsParseCorrectly() {
        // given
        Parameters expectedResult = new Parameters();
        expectedResult.setImageFileType("png");
        expectedResult.setImageSaveLocation("D:\\Games");
        expectedResult.setTextFilePaths(Arrays.asList("D:\\test.txt", "D:\\test1.txt", "D:\\test2.txt"));

        // when
        Parameters actualResult = ArgumentsParser.parseArguments(new String[] {"--file-type", "png", "--file-path", "D:\\test.txt", "D:\\test1.txt", "D:\\test2.txt", "--save-location", "D:\\Games"});

        // then
        assertEquals(expectedResult.getImageFileType(), actualResult.getImageFileType());
        assertEquals(expectedResult.getImageSaveLocation(), actualResult.getImageSaveLocation());
        assertEquals(expectedResult.getAllTextFilePaths(), actualResult.getAllTextFilePaths());
    }

    @Test
    public void verifyHelpIsShownWhenNoArgumentIsProvided() throws IOException, InterruptedException {
        // given
        String data = "";
        String fileName = "test.txt";
        String saveFolderName = "saveLocationForTests";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        URL saveFolderURL = classLoader.getResource(saveFolderName);
        assert resource != null;
        String path = resource.getPath();
        var args = new String[] {"--file-path", path, "--save-location", String.valueOf(saveFolderURL)};

        // then
        assertThrows(IllegalArgumentException.class, () -> ArgumentsParser.parseArguments(args));
    }
}
