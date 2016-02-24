package com.ersen.persistencemodulesample.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.URLUtil;

import com.ersen.persistencemodulesample.models.Treat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileAndStorageUtils {

    public static String getFileNameFromUrl(@NonNull String url) {
        return URLUtil.guessFileName(url, null, null);
    }

    public static boolean isExternalStorageReady() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * Naive implementation of how to retrieve some data from the disk
     * It is more than enough to demonstrate the idea though.
     */
    @Nullable
    public static InputStream getInputStream(@NonNull Treat treat) {
        try {
            String filePathToImage = treat.getFilePathToImage();
            if (filePathToImage == null) return null;
            return new FileInputStream(filePathToImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
