<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="myEnvironment" scope="application" type="java.util.Map"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Servlet File Upload/Download</title>

        <link rel="stylesheet" href="resource/css/main.css" />
        <script type="text/javascript" src="resource/js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="resource/js/fileupload.js"></script>
    </head>
    <body>
        <div class="panel">
            <h1>File(s) Upload</h1>
            <form id="fileUploadForm" method="post" action="fileUploadServlet" enctype="multipart/form-data">
                <div class="form">
                    <label>Upload File(s)</label><span id="colon">: </span><input id="fileAttachment" type="file" name="fileUpload" multiple="multiple" />
                    <span id="fileUploadErr">Please Upload A File!</span>
                    <label>Image Extension</label><span id="colon1">: </span>
                    <select name="imageExtension" type="text" id="imageExtension">
                            <option value="png">.png</option>
                            <option value="jpg">.jpg</option>
                    </select><br>
                    <label for="saveLocation">Save Location</label>
                    <select name="saveLocation" id="saveLocation">
                        <c:forEach items="${myEnvironment}" var="location">
                            <option value=${location.value}>${location.value}</option>
                        </c:forEach>
                    </select>
                    <button id="uploadBtn" type="submit" class="btn btn_primary">Upload</button>
                </div>
            </form>
        </div>
        <div class="panel">
            <a id="allFiles" class="hyperLink" href="<%=request.getContextPath()%>/uploadedFilesServlet">List all uploaded files</a>
        </div>
    </body>
</html>