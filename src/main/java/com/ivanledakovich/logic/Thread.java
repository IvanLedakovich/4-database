package com.ivanledakovich.logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * This class handles the single thread logic
 *
 * @author Ivan Ledakovich
 */
public class Thread extends java.lang.Thread {

	private final FileService fileService = new FileService();

	private String imageFileType;
	private String imageSaveLocation;
	private String textFilePath;

	public Thread(String imageFileType, String imageSaveLocation, String textFilePath) {
		this.imageFileType = imageFileType;
		this.imageSaveLocation = imageSaveLocation;
		this.textFilePath = textFilePath;
	}

	public Thread() {}

	/**
	 * This method contains logic that is executed when a new thread is started
	 */
	@Override
	public void run() {
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
	}

	/**
	 * This method creates an object of type Thread and runs it
	 *
	 * @param imageFileType the desired type of output image
	 * @param imageSaveLocation the desired path for file saving
	 * @param textFilePath the path to the initial .txt file
	 */
	public void startANewThread(String imageFileType, String imageSaveLocation, String textFilePath) {
		Thread thread = new Thread(imageFileType, imageSaveLocation, textFilePath);
		thread.start();
	}
}