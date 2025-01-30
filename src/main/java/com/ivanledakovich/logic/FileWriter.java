package com.ivanledakovich.logic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileWriter {
    private static final String UPLOAD_DIR = System.getProperty("java.io.tmpdir") + "/uploadedFiles/";

    public static byte[] writeFile(ResultSet rs) throws SQLException, IOException {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String fileName = rs.getString("file_name");
        byte[] fileBytes = rs.getBytes("file_data");

        File outputFile = new File(uploadDir, fileName + ".txt");

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(fileBytes);
        }
        return fileBytes;
    }
}