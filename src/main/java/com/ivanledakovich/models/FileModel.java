package com.ivanledakovich.models;

import java.util.Date;

public class FileModel {
    private Date date;
    private String file_name;

    public void setDate(Date date) {
        this.date = date;
    }
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public Date getDate() {
        return this.date;
    }
    public String getFile_name() {
        return this.file_name;
    }
}
