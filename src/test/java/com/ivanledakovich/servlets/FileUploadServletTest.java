package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.ThreadStarter;
import com.ivanledakovich.logic.UploadDetail;
import com.ivanledakovich.logic.UploadedFilesProcessor;
import org.junit.Before;
import org.junit.Test;
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
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FileUploadServletTest {

    private FileUploadServlet servlet;

    @Mock
    private UploadedFilesProcessor uploadedFilesProcessor;

    @Mock
    private ThreadStarter threadStarter;

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
        servlet = new FileUploadServlet(uploadedFilesProcessor, threadStarter);
        responseWriter = new StringWriter();

        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath("")).thenReturn("/application/path");

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        servlet.init(servletConfig);
    }

    @Test
    public void verifyDoPostResponseIsCorrect() throws ServletException, IOException, SQLException {
        // Given
        when(request.getParameter("imageExtension")).thenReturn(".jpg");
        when(request.getParameter("saveLocation")).thenReturn("/save/location");
        when(request.getParts()).thenReturn(Collections.singletonList(part));
        when(part.getHeader("content-disposition")).thenReturn("form-data; name=\"file\"; filename=\"test.jpg\"");
        when(part.getSize()).thenReturn(1024L);
        doNothing().when(part).write(anyString());
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));

        List<UploadDetail> mockFileList = Collections.emptyList();
        when(uploadedFilesProcessor.processUploadedFiles(any(HttpServletRequest.class), anyString()))
                .thenReturn(mockFileList);

        doNothing().when(threadStarter).startThreads(anyString(), anyString(), anyString());

        // When
        servlet.doPost(request, response);

        // Then
        assertEquals("Input: .jpg /save/location", responseWriter.toString().trim());

        verify(response).getWriter();
        verify(session).setAttribute(eq("uploadedFiles"), any());
        verify(dispatcher).forward(request, response);
    }
}
