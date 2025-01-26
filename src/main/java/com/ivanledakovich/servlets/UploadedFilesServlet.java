package com.ivanledakovich.servlets;

import com.ivanledakovich.database.FileDatabaseFunctions;
import com.ivanledakovich.logic.UploadDetail;
import com.ivanledakovich.models.FileModel;
import com.ivanledakovich.utils.ConfigurationVariables;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
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

        FileDatabaseFunctions fileDatabaseFunctions = new FileDatabaseFunctions(ConfigurationVariables.getEnvironmentVariables());
        List<FileModel> fileModels;
        try {
            fileModels = fileDatabaseFunctions.getAllFiles();
        } catch (SQLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("fileModels", fileModels);

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward POST requests to the doGet method for handling
        doGet(request, response);
    }
}