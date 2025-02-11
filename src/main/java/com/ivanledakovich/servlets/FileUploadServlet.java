package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.FileProcessorStarter;
import com.ivanledakovich.logic.FileService;
import com.ivanledakovich.logic.UploadDetail;
import com.ivanledakovich.logic.UploadedFilesProcessor;
import com.ivanledakovich.utils.ConfigurationVariables;
import org.apache.log4j.Logger;

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
import java.util.List;

import static com.ivanledakovich.utils.FolderCreator.createFolder;

@WebServlet(description = "Upload File To The Server", urlPatterns = { "/fileUploadServlet" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10,
        maxFileSize = 1024 * 1024 * 30,
        maxRequestSize = 1024 * 1024 * 50)
public class FileUploadServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FileUploadServlet.class);

    private static final String UPLOAD_DIR = "uploadedFiles";
    private static final long MAX_FILE_AGE = 24 * 60 * 60 * 1000;

    private final UploadedFilesProcessor uploadedFilesProcessor;
    private final FileProcessorStarter fileProcessorStarter;

    private String getUploadPath() {
        return System.getProperty("java.io.tmpdir") + File.separator + UPLOAD_DIR;
    }

    public FileUploadServlet() {
        this.uploadedFilesProcessor = new UploadedFilesProcessor();
        this.fileProcessorStarter = new FileProcessorStarter(new FileService());
    }

    private void cleanTempFiles(String uploadPath) {
        File directory = new File(uploadPath);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            long now = System.currentTimeMillis();

            if (files != null) {
                for (File file : files) {
                    if (now - file.lastModified() > MAX_FILE_AGE) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uploadPath = getUploadPath();
        createFolder(uploadPath);

        cleanTempFiles(uploadPath);
        String imageExtension = request.getParameter("imageExtension");
        String saveLocation = ConfigurationVariables.getStorageType().equalsIgnoreCase("file_system")
                ? ConfigurationVariables.getStoragePath()
                : request.getParameter("saveLocation");

        createFolder(uploadPath);
        createFolder(saveLocation);

        List<UploadDetail> fileList = uploadedFilesProcessor.processUploadedFiles(request, uploadPath);

        request.setAttribute("uploadedFiles", fileList);
        HttpSession session = request.getSession();
        session.setAttribute("uploadedFiles", fileList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/uploadedFilesServlet");
        dispatcher.forward(request, response);

        try {
            fileProcessorStarter.startThreads(imageExtension, uploadPath, saveLocation);
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }

        response.getWriter().append("Input: " + imageExtension + " " + saveLocation);
    }
}