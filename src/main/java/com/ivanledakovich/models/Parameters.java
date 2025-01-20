package com.ivanledakovich.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the type to store the command line arguments in
 *
 * @author Ivan Ledakovich
 */
public class Parameters {
    private String imageFileType;
    private String imageSaveLocation;
    private List<String> textFilePaths = new ArrayList<String>();

    public String getImageFileType(){
        return this.imageFileType;
    }

    public String getImageSaveLocation(){
        return this.imageSaveLocation;
    }

    public List<String> getAllTextFilePaths(){
        return this.textFilePaths;
    }

    public String getSingleTextFilePath(int i){
        return this.textFilePaths.get(i);
    }

    public void setImageFileType(String imageFileType){
        this.imageFileType = imageFileType;
    }

    public void setImageSaveLocation(String imageSaveLocation){
        this.imageSaveLocation = imageSaveLocation;
    }

    public void setTextFilePaths(List<String> textFilePaths){
        this.textFilePaths = textFilePaths;
    }
}
