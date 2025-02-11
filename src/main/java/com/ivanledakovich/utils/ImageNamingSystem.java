package com.ivanledakovich.utils;

public class ImageNamingSystem {

    public static String nameAnImageByNameAndExtension(String txtFileName, String imageExtension) {
        return txtFileName + "." + imageExtension;
    }
}
