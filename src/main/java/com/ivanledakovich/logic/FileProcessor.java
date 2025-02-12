package com.ivanledakovich.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public class FileProcessor implements Callable<Integer> {

    private final FileService fileService = new FileService();

    private String imageFileType;
    private String imageSaveLocation;
    private String textFilePath;

    public FileProcessor(String imageFileType, String imageSaveLocation, String textFilePath) {
        this.imageFileType = imageFileType;
        this.imageSaveLocation = imageSaveLocation;
        this.textFilePath = textFilePath;
    }

    @Override
    public Integer call() throws Exception {
        try {
            File tempFile = new File(textFilePath);
            String imageName = tempFile.getName() + "." + imageFileType;

            String data = FileReader.readFile(textFilePath);
            BufferedImage image = ImageCreator.createImage(data);

            // Ensure output directory exists
            new File(imageSaveLocation).mkdirs();

            File tempImage = new File(imageSaveLocation, imageName);
            if (!ImageIO.write(image, imageFileType, tempImage)) {
                throw new IOException("No appropriate writer found for format: " + imageFileType);
            }

            fileService.insertFile(tempFile, tempImage);
            return 0;
        } catch (IOException | SQLException e) {
            ErrorNotifier.fileCouldNotBeWritten();
            throw e; // Re-throw to fail the test
        }
    }
}
