package com.ivanledakovich.models;

import java.util.Date;

public class FileModel {
    private int id;
    private Date date;
    private String fileName;
    private byte[] fileData;
    private String imageName;
    private String imageType;
    private byte[] imageData;

    public void setDate(Date date) {
        this.date = date;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
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

    public int getId() {
        return id;
    }

    public void setImageData(byte[] imageData) {
        System.out.println(imageData.length);
        this.imageData = imageData;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public byte[] getImageData() {
        return imageData;
    }

}
