package com.ivanledakovich.servlets;

import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(description = "Download File From The Server", urlPatterns = { "/downloadServlet" })
public class FileDownloadServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FileDownloadServlet.class);
    private static final long serialVersionUID = 1L;

    private static final int BUFFER_SIZE = 1024 * 100;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = request.getParameter("fileName"), filePath = getClass().getClassLoader().getResource("uploadedFiles").getPath() + File.separator + fileName;

        File file = new File(filePath);
        OutputStream outStream = null;
        FileInputStream inputStream = null;

        if (file.exists()) {
            String mimeType = "application/octet-stream";
            response.setContentType(mimeType);

            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
            response.setHeader(headerKey, headerValue);

            try {
                outStream = response.getOutputStream();
                inputStream = new FileInputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
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
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3>File "+ fileName +" Does Not Exist</h3>");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}