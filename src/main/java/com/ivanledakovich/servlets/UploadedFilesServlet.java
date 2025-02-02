package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.FileService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

@WebServlet("/uploadedFilesServlet")
public class UploadedFilesServlet extends HttpServlet {
    private FileService fileService = new FileService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setAttribute("fileModels", fileService.getAllFiles());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/allfiles.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException | URISyntaxException e) {
            throw new ServletException("Error retrieving files", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}