package com.ivanledakovich.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileExtractor {
    private static final Logger logger = Logger.getLogger(FileExtractor.class);

    public static void extractFile(HttpServletResponse response, File file) throws IOException {
        OutputStream outStream = null;
        FileInputStream inputStream = null;

        String mimeType = "application/octet-stream";
        response.setContentType(mimeType);

        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
        response.setHeader(headerKey, headerValue);

        try {
            outStream = response.getOutputStream();
            inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024 * 100];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                if (outStream != null) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
        } catch(IOException ioExObj) {
            logger.error("Exception While Performing The I/O Operation?= " + ioExObj.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
        }
    }
}
