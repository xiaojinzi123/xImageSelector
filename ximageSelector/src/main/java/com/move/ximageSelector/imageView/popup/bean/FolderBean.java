package com.move.ximageSelector.imageView.popup.bean;

/**
 * Created by cxj on 2016/10/9.
 */
public class FolderBean {

    private String firstImagePath;

    private String folderPath;

    private String folderName;

    private int numberOfImagesInFolder;

    private boolean isChoosed;

    public FolderBean() {
    }

    public FolderBean(String firstImagePath, String folderName, int numberOfImagesInFolder, boolean isChoosed) {
        this.firstImagePath = firstImagePath;
        this.folderName = folderName;
        this.numberOfImagesInFolder = numberOfImagesInFolder;
        this.isChoosed = isChoosed;
    }

    public FolderBean(String firstImagePath, String folderPath, String folderName, int numberOfImagesInFolder, boolean isChoosed) {
        this.firstImagePath = firstImagePath;
        this.folderPath = folderPath;
        this.folderName = folderName;
        this.numberOfImagesInFolder = numberOfImagesInFolder;
        this.isChoosed = isChoosed;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getNumberOfImagesInFolder() {
        return numberOfImagesInFolder;
    }

    public void setNumberOfImagesInFolder(int numberOfImagesInFolder) {
        this.numberOfImagesInFolder = numberOfImagesInFolder;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }
}
