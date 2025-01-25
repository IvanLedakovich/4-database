package com.ivanledakovich.models;

import java.util.Date;

public class FileModel {
    private Date date;
    private String fileName;
    private byte[] fileData;

    public void setDate(Date date) {
        this.date = date;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDate() {
        return this.date;
    }
    public String getFileName() {
        return this.fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
