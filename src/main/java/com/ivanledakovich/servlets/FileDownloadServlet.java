package com.ivanledakovich.servlets;

import com.ivanledakovich.utils.FileExtractor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@WebServlet(description = "Download File From The Server", urlPatterns = { "/downloadServlet" })
public class FileDownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String fileName = request.getParameter("fileName"), filePath = getClass().getClassLoader().getResource("uploadedFiles").getPath() + File.separator + fileName;

        File file = new File(filePath);
        if (file.exists()) {
            FileExtractor.extractFile(response, file);
        } else {
            response.setContentType("text/html");
            response.getWriter().println("<h3>File "+ fileName +" Does Not Exist</h3>");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}