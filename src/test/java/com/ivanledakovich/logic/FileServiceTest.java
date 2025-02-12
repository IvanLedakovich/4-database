package com.ivanledakovich.logic;

import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ConfigurationVariables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

public class FileServiceTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File uploadDir;
    private byte[] controlSampleBytes;

    @Before
    public void setUp() throws Exception {
        String uniqueDbName = "testdb-" + UUID.randomUUID();
        Properties testProps = new Properties();
        testProps.setProperty("STORAGE_TYPE", "database");
        testProps.setProperty("DB_URL", "jdbc:h2:mem:" + uniqueDbName + ";DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        testProps.setProperty("DB_DRIVER", "org.h2.Driver");
        testProps.setProperty("DB_USERNAME", "sa");
        testProps.setProperty("DB_PASSWORD", "");

        Field propertiesField = ConfigurationVariables.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        propertiesField.set(null, testProps);

        uploadDir = tempFolder.newFolder("upload");
        copyTestResourceToDirectory("test.txt", uploadDir);
        copyTestResourceToDirectory("test.json", uploadDir);

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("control-sample.png")) {
            assertNotNull("Control image missing", is);
            controlSampleBytes = readAllBytes(is);
        }
    }

    private void copyTestResourceToDirectory(String resourceName, File destinationDir) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            Files.copy(is, new File(destinationDir, resourceName).toPath());
        }
    }

    private byte[] readAllBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int read;
        while ((read = is.read(data)) != -1) {
            buffer.write(data, 0, read);
        }
        return buffer.toByteArray();
    }

    @Test
    public void testTextFileConversionMatchesControlSample() throws Exception {
        FileService fileService = new FileService();

        fileService.processTxtFilesIntoImages(
                "png",
                uploadDir.getAbsolutePath(),
                tempFolder.newFolder("converted").getAbsolutePath()
        );

        verifyFileConversion(fileService, "test.txt");
        verifyFileConversion(fileService, "test.json");
    }

    private void verifyFileConversion(FileService service, String fileName) throws Exception {
        FileModel model = service.getFile(fileName);
        assertNotNull("File should exist: " + fileName, model);
        assertArrayEquals(
                "Image data mismatch for " + fileName,
                controlSampleBytes,
                model.getImageData()
        );
    }
}