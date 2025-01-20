package com.ivanledakovich.logic;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class contains the method which writes the generated image
 *
 * @author Ivan Ledakovich
 */
public class ImageWriter {

    private static final Logger logger = Logger.getLogger(ImageWriter.class);

    /**
     * This method writes the final image file
     *
     * @param image generated image
     * @param imageFileType image file type for saving
     * @param imageSaveLocation locations to save the image
     * @param textFilePath the path to the initial .txt file
     */
    public static void writeImage(BufferedImage image, String imageFileType, String imageSaveLocation, String textFilePath) {
        try {
            if(!Files.exists(Path.of(imageSaveLocation))) throw new IOException();
            ImageIO.write(image, imageFileType, new File(imageSaveLocation + "\\" + textFilePath.substring(textFilePath.lastIndexOf("\\")+1) + "." + imageFileType));
        } catch (IOException e) {
            ErrorNotifier.fileCouldNotBeWritten();
            logger.error(e);
        }
    }
}
