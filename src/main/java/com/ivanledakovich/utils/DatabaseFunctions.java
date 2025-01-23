package com.ivanledakovich.utils;

import com.ivanledakovich.models.DatabaseBean;
import com.ivanledakovich.models.FileModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFunctions {

    DatabaseBean databaseBean = new DatabaseBean();

    public DatabaseFunctions(){
        databaseBean.setUrl(System.getenv().get("DB_URL"));
        databaseBean.setUsername(System.getenv().get("DB_USERNAME"));
        databaseBean.setPassword(System.getenv().get("DB_PASSWORD"));
        databaseBean.setDriver(System.getenv().get("DB_DRIVER"));
    }

    public Connection connect() {
            try {
                Class.forName(databaseBean.getDriver());
                Connection con = DriverManager.getConnection(databaseBean.getUrl(), databaseBean.getUsername(), databaseBean.getPassword());
                return con;
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
    }

    public static void insertAFile(File child) throws SQLException, IOException {
        String file_name = child.getName();
        FileInputStream fis = new FileInputStream(child);
        DatabaseFunctions databaseFunctions = new DatabaseFunctions();
        Connection con = databaseFunctions.connect();
        PreparedStatement prtmt = con.prepareStatement("INSERT INTO files(file_name, file_data) VALUES (?, ?)");
        prtmt.setString(1, file_name);
        prtmt.setBinaryStream(2, fis, (int) child.length());
        prtmt.executeUpdate();
        fis.close();
    }

    public static List<FileModel> getAllFiles() throws SQLException, ClassNotFoundException, IOException, URISyntaxException {
        DatabaseFunctions databaseFunctions = new DatabaseFunctions();
        Connection con = databaseFunctions.connect();
        PreparedStatement prtmt;
        prtmt = con.prepareStatement("SELECT * FROM files");
        ResultSet rs = prtmt.executeQuery();
        List<FileModel> files = new ArrayList<>();
        while(rs.next()) {
            FileModel file = new FileModel();
            URL res = DatabaseFunctions.class.getClassLoader().getResource("uploadedFiles");
            File newfile = Paths.get(res.toURI()).toFile();
            String absolutePath = newfile.getAbsolutePath();
            File fileUploadDirectory = new File(absolutePath);
            if (!fileUploadDirectory.exists()) {
                fileUploadDirectory.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(absolutePath + "//" + rs.getString("file_name") + ".txt");
            byte[] fileBytes = rs.getBytes("file_data");
            fos.write(fileBytes);
            file.setDate(rs.getDate("creation_date"));
            file.setFile_name(rs.getString("file_name"));
            files.add(file);
        }
        return files;
    }
}
