package com.ivanledakovich.servlets;

import com.ivanledakovich.logic.FileService;
import com.ivanledakovich.models.FileModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileDownloadServletTest {
    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private FileService fileService;
    @Mock private ServletOutputStream outputStream;

    @InjectMocks
    private FileDownloadServlet servlet;

    @Test
    public void whenRequestedFileIsImage_servesImageCorrectly() throws Exception {
        FileModel mockFile = new FileModel();
        mockFile.setFileName("test.txt");
        mockFile.setImageName("test.png");
        mockFile.setImageType("png");
        mockFile.setImageData(new byte[10]);

        when(request.getParameter("fileName")).thenReturn("test.png");
        when(fileService.getFile("test.png")).thenReturn(mockFile);
        when(response.getOutputStream()).thenReturn(outputStream);

        servlet.doGet(request, response);

        verify(response).setContentType("image/png");
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"test.png\"");
        verify(outputStream).write(any(byte[].class));
    }
}