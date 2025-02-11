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

            File tempImage = new File(System.getProperty("java.io.tmpdir"), imageName);
            ImageIO.write(image, imageFileType, tempImage);

            fileService.insertFile(tempFile, tempImage);

            tempFile.delete();
            tempImage.delete();

        } catch (IOException | SQLException e) {
            ErrorNotifier.fileCouldNotBeWritten();
        }
        return 0;
    }
}
