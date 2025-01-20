package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.UploadDetail;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(description = "List The Already Uploaded Files", urlPatterns = { "/uploadedFilesServlet" })
public class UploadedFilesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "uploadedFiles";
    private static final String CONVERTED_DIR = "convertedFiles";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String applicationPath = getServletContext().getRealPath(""),
                uploadPath = applicationPath + File.separator + UPLOAD_DIR,
                convertedPath = applicationPath + CONVERTED_DIR;

        File fileUploadDirectory = new File(uploadPath);
        if (!fileUploadDirectory.exists()) {
            fileUploadDirectory.mkdirs();
        }
        File fileConvertedDirectory = new File(convertedPath);
        if (!fileConvertedDirectory.exists()) {
            fileConvertedDirectory.mkdirs();
        }

        UploadDetail details = null;
        File[] allFiles = fileUploadDirectory.listFiles();
        List<UploadDetail> fileList = new ArrayList<UploadDetail>();

        for (File file : allFiles) {
            details = new UploadDetail();
            details.setFileName(file.getName());
            details.setFileSize(file.length() / 1024);
            fileList.add(details);
        }

        request.setAttribute("uploadedFiles", fileList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/allfiles.jsp");
        dispatcher.forward(request, response);
    }
}