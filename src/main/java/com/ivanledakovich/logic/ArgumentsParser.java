package com.ivanledakovich.logic;

import com.ivanledakovich.models.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods to parse command line arguments
 *
 * @author Ivan Ledakovich
 */
public class ArgumentsParser {

    /**
     * This method converts command line arguments into a Parameter class object
     *
     * @param args command line arguments
     * @return command line arguments in form of a structured object of class Parameters
     */
    public static Parameters parseArguments(String[] args) {

        Parameters parameters = new Parameters();

        for (int i = 0; i < args.length; i++) {
            if(args[i].equalsIgnoreCase("--help") || args[i].equalsIgnoreCase("help")){
                Help.help();
            }
            if (args[i].equalsIgnoreCase("--file-type")) {
                parameters.setImageFileType(args[i + 1]);
            }
            if (args[i].equalsIgnoreCase("--save-location")) {
                parameters.setImageSaveLocation(args[i + 1]);
            }
            if (args[i].equalsIgnoreCase("--file-path")) {
                parameters.setTextFilePaths(parseFilePaths(args, i));
            }
        }

        if(parameters.getImageFileType() == null || parameters.getImageSaveLocation() == null || parameters.getAllTextFilePaths() == null){
            Help.help();
            throw new IllegalArgumentException();
        }

        if(!(parameters.getImageFileType().equalsIgnoreCase("png") || parameters.getImageFileType().equalsIgnoreCase("jpg"))){
            ErrorNotifier.invalidFileTypeNotification();
        }
            return parameters;
    }

    /**
     * This method converts command line file paths into a List of Strings
     *
     * @param args command line arguments
     * @param currentArgumentIndex current argument index in command line
     * @return List of file paths
     */
    private static List<String> parseFilePaths(String[] args, int currentArgumentIndex) {
        List<String> filePaths = new ArrayList<String>();

        for (int i = currentArgumentIndex + 1; i < args.length; i++) {
            if (args[i].contains("--")) {
                break;
            } else {
                filePaths.add(args[i]);
            }
        }
        return filePaths;
    }
}
