package com.ersen.persistencemodulesample.models;

import android.support.annotation.Nullable;

public class Treat {
    private String mName;
    private String mImageUrl;
    private String mFilePathToImage;

    public Treat(String mName, String mImageUrl) {
        this.mName = mName;
        this.mImageUrl = mImageUrl;
    }

    @Override
    public String toString() {
        return String.format("Treat Name: %s, Treat Image Url: %s, Treat Image File Path: %s", getName(), getImageUrl(), getFilePathToImage());
    }

    public void setFilePathToImage(String mFilePathToImage) {
        this.mFilePathToImage = mFilePathToImage;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    @Nullable
    public String getFilePathToImage() {
        return mFilePathToImage;
    }
}
