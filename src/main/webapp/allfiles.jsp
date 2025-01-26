<%@page import="java.util.List"%>
<%@ page import="com.ivanledakovich.models.FileModel" %>
<%@ page import="com.ivanledakovich.database.FileDatabaseFunctions" %>
<%@ page import="com.ivanledakovich.utils.ConfigurationVariables" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Servlet File Upload/Download</title>

        <link rel="stylesheet" href="resource/css/main.css" />
    </head>
    <body>
        <div class="panel">
            <h1>Uploaded Files</h1>
            <table class="bordered_table">
               <thead>
                  <tr align="center"><th>Creation Date</th><th>File Name</th><th>Download</th></tr>
               </thead>
               <tbody>
                  <%List<FileModel> fileModels = (List<FileModel>) request.getAttribute("fileModels");
                    if(fileModels.size() > 0) {
                      for (int i = 0; i < fileModels.size(); i++) {
                  %>
                   <tr>
                      <td align="center"><span id="creationDate"><%=fileModels.get(i).getDate()%></span></td>
                      <td align="center"><span id="fileName"><%=fileModels.get(i).getFileName()%></span></td>
                       <td align="center"><span id="fileDownload"><a id="downloadLink" class="hyperLink" href="<%=request.getContextPath()%>/downloadServlet?fileName=<%=fileModels.get(i).getFileName()%>.txt">Download</a></span></td>
                   <% }
                      } else { %>
                  <tr>
                      <td colspan="3" align="center"><span id="noFiles">No Files Uploaded Yet</span></td>
                  </tr>
                  <% } %>
               </tbody>
            </table>
            <div class="margin_top_15px">
               <a id="fileUpload" class="hyperLink" href="<%=request.getContextPath()%>/fileupload.jsp">Back</a>
            </div>
         </div>
     </body>
</html>