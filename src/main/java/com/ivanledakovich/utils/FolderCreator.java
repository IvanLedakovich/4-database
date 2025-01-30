package com.ivanledakovich.utils;

import java.io.File;

public class FolderCreator {
    public static void createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
