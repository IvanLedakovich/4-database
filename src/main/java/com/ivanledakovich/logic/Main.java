package com.ivanledakovich.logic;

import com.ivanledakovich.models.Parameters;

public class Main {

    public static void main(String[] args) throws Exception {
        Parameters parameters = ArgumentsParser.parseArguments(args);
        for (int i = 0; i < parameters.getAllTextFilePaths().size(); i++) {
            FileProcessor test = new FileProcessor(parameters.getImageFileType(), parameters.getImageSaveLocation(), parameters.getSingleTextFilePath(i));
            test.call();
        }
    }
}
