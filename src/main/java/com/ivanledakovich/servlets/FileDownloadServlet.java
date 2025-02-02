package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.FileService;
import com.ivanledakovich.models.FileModel;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/downloadServlet")
public class FileDownloadServlet extends HttpServlet {
    private FileService fileService = new FileService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        try {
            FileModel file = fileService.getFile(fileName);
            if (file != null && file.getFileData() != null) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                response.getOutputStream().write(file.getFileData());
            } else {
                sendError(response, fileName);
            }
        } catch (SQLException e) {
            sendError(response, fileName);
        }
    }

    private void sendError(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h3>File " + fileName + " Does Not Exist</h3>");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}