package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.ThreadStarter;
import com.ivanledakovich.logic.UploadDetail;
import com.ivanledakovich.logic.UploadedFilesProcessor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.ivanledakovich.utils.FolderCreator.createFolder;

@WebServlet(description = "Upload File To The Server", urlPatterns = { "/fileUploadServlet" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, maxFileSize = 1024 * 1024 * 30, maxRequestSize = 1024 * 1024 * 50)
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "uploadedFiles";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageExtension = request.getParameter("imageExtension");
        String saveLocation = request.getParameter("saveLocation");

        String applicationPath = getServletContext().getRealPath(""), uploadPath = applicationPath + File.separator + UPLOAD_DIR;

        createFolder(uploadPath);
        createFolder(saveLocation);

        UploadedFilesProcessor uploadedFilesProcessor = new UploadedFilesProcessor();
        List<UploadDetail> fileList = uploadedFilesProcessor.processUploadedFiles(request, uploadPath);

        request.setAttribute("uploadedFiles", fileList);
        HttpSession session = request.getSession();
        session.setAttribute("uploadedFiles", fileList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/uploadedFilesServlet");
        dispatcher.forward(request, response);
        ThreadStarter threadStarter = new ThreadStarter();
        try {
            threadStarter.startThreads(imageExtension, uploadPath, saveLocation);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        response.getWriter().append("Input: " + imageExtension + " " + saveLocation);
    }
}