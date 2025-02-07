package com.ivanledakovich.servlets;

import com.ivanledakovich.utils.ConfigurationVariables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadServletTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private ServletConfig servletConfig;
    @Mock private ServletContext servletContext;
    @Mock private Part part;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher requestDispatcher;

    private FileUploadServlet servlet;
    private StringWriter responseWriter;

    @Before
    public void setUp() throws Exception {
        File storageFolder = tempFolder.newFolder("storage");
        ConfigurationVariables.getConfigProperties().setProperty("STORAGE_PATH", storageFolder.getAbsolutePath());
        ConfigurationVariables.getConfigProperties().setProperty("STORAGE_TYPE", "file_system");

        servlet = new FileUploadServlet();
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath(anyString())).thenReturn(tempFolder.newFolder("webapp").getAbsolutePath());
        servlet.init(servletConfig);

        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(request.getSession()).thenReturn(session);

        when(request.getRequestDispatcher(eq("/uploadedFilesServlet"))).thenReturn(requestDispatcher);

        when(request.getParts()).thenReturn(Collections.singleton(part));
        lenient().when(part.getHeader(eq("content-disposition")))
                .thenReturn("form-data; name=\"file\"; filename=\"test.txt\"");
    }

    @Test
    public void verifyDoPostResponseIsCorrect() throws Exception {
        // Given
        when(request.getParameter("imageExtension")).thenReturn("jpg");

        // When
        servlet.doPost(request, response);

        // Then
        String result = responseWriter.toString();
        assertTrue("Response should contain processed parameters",
                result.contains("Input: jpg " + ConfigurationVariables.getStoragePath()));

        verify(requestDispatcher).forward(request, response);
    }
}