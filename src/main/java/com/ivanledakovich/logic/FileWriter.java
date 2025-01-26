package com.ivanledakovich.logic;

import com.ivanledakovich.database.FileDatabaseFunctions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileWriter {
    public static byte[] writeFile(ResultSet rs) throws URISyntaxException, SQLException, IOException {
        URL res = FileDatabaseFunctions.class.getClassLoader().getResource("uploadedFiles");
        File newFile = Paths.get(res.toURI()).toFile();
        String absolutePath = newFile.getAbsolutePath();
        File fileUploadDirectory = new File(absolutePath);
        if (!fileUploadDirectory.exists()) {
            fileUploadDirectory.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(absolutePath + "//" + rs.getString("file_name") + ".txt");
        byte[] fileBytes = rs.getBytes("file_data");
        fos.write(fileBytes);
        fos.close();
        return fileBytes;
    }
}
