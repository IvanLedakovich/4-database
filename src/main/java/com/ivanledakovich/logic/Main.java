package com.ivanledakovich.logic;

import com.ivanledakovich.models.Parameters;

public class Main {

    public static void main(String[] args) {
        Parameters parameters = ArgumentsParser.parseArguments(args);
        for (int i = 0; i < parameters.getAllTextFilePaths().size(); i++) {
            Thread thread = new Thread();
            thread.startANewThread(parameters.getImageFileType(), parameters.getImageSaveLocation(), parameters.getSingleTextFilePath(i));
        }
    }
}
