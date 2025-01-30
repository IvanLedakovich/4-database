package com.ivanledakovich.logic;

import com.ivanledakovich.utils.FileNameExtractor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadedFilesProcessor {
    public List<UploadDetail> processUploadedFiles(HttpServletRequest request, String uploadPath) throws ServletException, IOException {
        List<UploadDetail> fileList = new ArrayList<>();

        for (Part part : request.getParts()) {
            String fileName = FileNameExtractor.extractFileName(part);

            if (fileName == null || fileName.isEmpty()) {
                continue; // Skip empty file parts
            }

            UploadDetail details = new UploadDetail();
            details.setFileName(fileName);
            details.setFileSize(part.getSize() / 1024);

            try {
                part.write(uploadPath + File.separator + fileName);
                details.setUploadStatus("Success");
            } catch (IOException ioException) {
                details.setUploadStatus("Failure: " + ioException.getMessage());
            }

            fileList.add(details);
        }

        return fileList;
    }
}
