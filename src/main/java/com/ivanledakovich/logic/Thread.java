package com.ivanledakovich.logic;

import java.awt.image.BufferedImage;

/**
 * This class handles the single thread logic
 *
 * @author Ivan Ledakovich
 */
public class Thread extends java.lang.Thread {

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
		String data = FileReader.readFile(textFilePath);
		BufferedImage image = ImageCreator.createImage(data);
		ImageWriter.writeImage(image, imageFileType, imageSaveLocation, textFilePath);
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