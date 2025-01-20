package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.ThreadStarter;
import com.ivanledakovich.logic.UploadDetail;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(description = "Upload File To The Server", urlPatterns = { "/fileUploadServlet" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, maxFileSize = 1024 * 1024 * 30, maxRequestSize = 1024 * 1024 * 50)
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "uploadedFiles";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageExtension = request.getParameter("imageExtension");
        String saveLocation = request.getParameter("saveLocation");

        String applicationPath = getServletContext().getRealPath(""), uploadPath = applicationPath + File.separator + UPLOAD_DIR;

        File fileUploadDirectory = new File(uploadPath);
        if (!fileUploadDirectory.exists()) {
            fileUploadDirectory.mkdirs();
        }

        File fileSaveDirectory = new File(saveLocation);
        if (!fileSaveDirectory.exists()) {
            fileSaveDirectory.mkdirs();
        }

        String fileName = "";
        UploadDetail details = null;
        List<UploadDetail> fileList = new ArrayList<UploadDetail>();

        for (Part part : request.getParts()) {
            fileName = extractFileName(part);
            details = new UploadDetail();
            details.setFileName(fileName);
            details.setFileSize(part.getSize() / 1024);
            try {
                part.write(uploadPath + File.separator + fileName);
                details.setUploadStatus("Success");
                fileList.add(details);
            } catch (IOException ioObj) {
                details.setUploadStatus("Failure : "+ ioObj.getMessage());
            }
        }

        request.setAttribute("uploadedFiles", fileList);
        HttpSession session = request.getSession();
        session.setAttribute("uploadedFiles", fileList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/allfiles.jsp");
        dispatcher.forward(request, response);
        ThreadStarter threadStarter = new ThreadStarter();
        try {
            threadStarter.startThreads(imageExtension, uploadPath, saveLocation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().append("Input: " + imageExtension + " " + saveLocation);
    }

    private String extractFileName(Part part) {
        String fileName = "",
                contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                fileName = item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return fileName;
    }
}