package com.ivanledakovich.servlets;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FileUploadServletTest {

    @InjectMocks
    private FileUploadServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Part part;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private ServletContext servletContext;

    private StringWriter responseWriter;

    @Before
    public void setUp() throws ServletException {
        MockitoAnnotations.initMocks(this);
        responseWriter = new StringWriter();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        servlet.init(servletConfig);
    }

    @Test
    public void verifyDoPostResponseIsCorrect() throws ServletException, IOException {
        // given
        when(request.getParameter("imageExtension")).thenReturn(".jpg");
        when(request.getParameter("saveLocation")).thenReturn("/save/location");
        when(request.getParts()).thenReturn(Collections.singletonList(part));
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"test.jpg\"");
        when(part.getSize()).thenReturn(1024L);
        doNothing().when(part).write(anyString());

        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/fileuploadResponse.jsp")).thenReturn(dispatcher);

        // when
        servlet.doPost(request, response);

        // then
        assertEquals("Input: .jpg /save/location", responseWriter.toString().trim());

        verify(response).getWriter();
        verify(dispatcher).forward(request, response);
        verify(session).setAttribute(eq("uploadedFiles"), anyList());
    }
}