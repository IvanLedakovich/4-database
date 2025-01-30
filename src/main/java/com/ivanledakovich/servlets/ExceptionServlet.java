package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.ErrorProcessor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;

@WebServlet("/Exception")
public class ExceptionServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ErrorProcessor errorProcessor = new ErrorProcessor();
        errorProcessor.processError(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}