package com.ivanledakovich.servlets;

import com.ivanledakovich.utils.ConfigurationVariables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FileUploadServletTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private FileUploadServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        servlet = new FileUploadServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        Part part = mock(Part.class);

        File storageFolder = tempFolder.newFolder("storage");
        ConfigurationVariables.getConfigProperties().setProperty("STORAGE_PATH", storageFolder.getAbsolutePath());
        ConfigurationVariables.getConfigProperties().setProperty("STORAGE_TYPE", "file_system");

        // Valid mock setup
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getParameter("imageExtension")).thenReturn("png");
        when(request.getParameter("saveLocation")).thenReturn(storageFolder.getAbsolutePath());
        when(request.getParts()).thenReturn(Collections.singleton(part));
        lenient().when(part.getHeader("content-disposition")).thenReturn("filename=\"test.txt\"");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getRequestDispatcher(anyString())).thenReturn(mock(RequestDispatcher.class));
    }

    @Test
    public void verifyFileUploadProcessesCorrectly() throws Exception {
        servlet.doPost(request, response);
        verify(response).getWriter();
    }
}