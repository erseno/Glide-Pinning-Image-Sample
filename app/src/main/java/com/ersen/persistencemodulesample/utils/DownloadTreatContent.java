package com.ersen.persistencemodulesample.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.ersen.persistencemodulesample.PersistenceModuleSampleApplication;
import com.ersen.persistencemodulesample.constants.AppConstants;
import com.ersen.persistencemodulesample.models.Treat;
import com.google.gson.Gson;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

public class DownloadTreatContent extends AsyncTask<Void, Void, Integer> {
    public static final int SUCCESS = 1, FAILED = 2;
    private static final String URL_TO_IMAGES_ZIP_FILE = "http://botfactory.co.uk/hosting/glide_peristance_sample.zip"; //This is the link to the zip file which contains all the images used in the app
    private ArrayList<Treat> mTreats;
    private WeakReference<OnTreatContentDownloaded> mWeakCallback; //Avoids possible memory leak since an activity reference will be held

    public DownloadTreatContent(Activity activity, ArrayList<Treat> mTreats) {
        if (activity instanceof OnTreatContentDownloaded) {
            OnTreatContentDownloaded onTreatContentDownloaded = (OnTreatContentDownloaded) activity;
            this.mWeakCallback = new WeakReference<>(onTreatContentDownloaded);
            this.mTreats = mTreats;
        } else {
            throw new ClassCastException("Activity must implement " + OnTreatContentDownloaded.class.getSimpleName());
        }
    }

    @Override
    protected void onPreExecute() {
        OnTreatContentDownloaded onTreatContentDownloaded = mWeakCallback.get();
        if (onTreatContentDownloaded != null) {
            onTreatContentDownloaded.onTreatContentDownloadStarted();
        }
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        /** Download the zip file */
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL_TO_IMAGES_ZIP_FILE)
                .build();

        File tempZipFile = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                /** Get the downloaded zip file from bytes */
                BufferedSource bufferedSource = response.body().source();
                tempZipFile = new File(PersistenceModuleSampleApplication.getInstance().getCacheDir(), FileAndStorageUtils.getFileNameFromUrl(URL_TO_IMAGES_ZIP_FILE));
                BufferedSink sink = Okio.buffer(Okio.sink(tempZipFile));
                sink.writeAll(bufferedSource);
                sink.close();

                /** Create a directory within the app's external private directory so that these files are removed when the app is deleted etc. */
                ZipFile zipFile = new ZipFile(tempZipFile);
                String treatImageFilePath = PersistenceModuleSampleApplication.getInstance().getExternalFilesDir(null).getAbsolutePath() + File.separator + "treats"; //The directory which will contain the images is called treats. Note ignore null warning because it is checked if external storage is ready before starting this task
                File treatImageDirectory = new File(treatImageFilePath);

                if (!treatImageDirectory.exists()) {
                    treatImageDirectory.mkdirs();
                }

                /** Using the Zip4j library to extract the contents of the zip file to the directory made above  */
                zipFile.extractAll(treatImageDirectory.getAbsolutePath());

                /** Relate the treat name to the file image which is now stored in the directory to set the absolute file path for that treat image. Note that the image file names were hard coded to match the names of the treats*/
                File[] allTreatImages = treatImageDirectory.listFiles(); // Get all the images that is in the treat images directory
                for (Treat treat : mTreats) {  // For each treat object
                    for (File file : allTreatImages) { //For each file stored in the directory
                        if (file.isFile()) { //Check if its file and not a directory for safety
                            String filename = file.getName();

                            if (filename.indexOf(".") > 0) { //Remove the extension i.e. KitKat.jpg would become KitKat
                                filename = filename.substring(0, filename.lastIndexOf("."));
                            }

                            if (treat.getName().equals(filename)) { //If the treat name is the same as the file name then its a match
                                treat.setFilePathToImage(file.getAbsolutePath()); //Get the absolute file path for this file and set it for the treat object it is related to
                                break;
                            }
                        }
                    }
                }

                /** Store the entire array list of treat objects in shared preferences via GSON by converting it as a JSON string */
                Gson gson = new Gson();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PersistenceModuleSampleApplication.getInstance().getApplicationContext()).edit();
                editor.putString(AppConstants.PREF_TREATS_KEY, gson.toJson(mTreats));
                editor.apply();

            } else { /** non http 200 status - Notify download failed */
                return FAILED;
            }
        } catch (IOException e) { /** Network Error - Notify download failed */
            e.printStackTrace();
            return FAILED;
        } catch (ZipException e) { /** Zip extraction had an error - Notify download failed and delete the temp file */
            e.printStackTrace();
            if(tempZipFile.exists()){
                tempZipFile.delete();
            }
            return FAILED;
        }

        tempZipFile.delete();
        return SUCCESS;
    }

    @Override
    protected void onPostExecute(Integer status) {
        OnTreatContentDownloaded onTreatContentDownloaded = mWeakCallback.get();
        if (onTreatContentDownloaded != null) {
            onTreatContentDownloaded.onTreatContentDownloadFinished(status);
        }
    }

    public interface OnTreatContentDownloaded {
        void onTreatContentDownloadStarted();

        void onTreatContentDownloadFinished(int status);
    }

}
