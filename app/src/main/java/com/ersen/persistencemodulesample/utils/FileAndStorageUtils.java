package com.ersen.persistencemodulesample.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.webkit.URLUtil;

public class FileAndStorageUtils {

    public static String getFileNameFromUrl(@NonNull String url) {
        return URLUtil.guessFileName(url, null, null);
    }

    public static boolean isExternalStorageReady(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
