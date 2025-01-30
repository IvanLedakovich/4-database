package com.ivanledakovich.utils;

import com.ivanledakovich.logic.UploadDetail;

import java.io.File;
import java.util.List;

public class ListPopulator {
    public static void populateListWithFiles(File[] allFiles, List<UploadDetail> fileList) {
        UploadDetail details;
        for (File file : allFiles) {
            details = new UploadDetail();
            details.setFileName(file.getName());
            details.setFileSize(file.length() / 1024);
            fileList.add(details);
        }
    }
}
