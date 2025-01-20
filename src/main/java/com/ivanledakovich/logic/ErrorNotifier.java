package com.ivanledakovich.logic;

import org.apache.log4j.Logger;

public class ErrorNotifier {
    private static final Logger logger = Logger.getLogger(ErrorNotifier.class);

    public static void invalidInputFilesNotification() {
        logger.error("Please check if the input file(s) are valid, of .txt format and not empty.");
    }

    public static void invalidFileTypeNotification() {
        logger.error("Please check if the selected image format is correct. The program accepts png and jpg format selection.");
    }

    public static void fileCouldNotBeWritten() {
        logger.error("Could not write the file. Please check if the provided save directory exists and is writable.");
    }
}
